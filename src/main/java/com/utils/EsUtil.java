package com.utils;

import com.config.ESConfigFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class EsUtil {

    private static TransportClient client;
    private static RestClient s_LowClient;
    private static RestHighLevelClient s_HighClient;

    private static SearchRequest searchRequest;
    private static SearchSourceBuilder sourceBuilder;
    private static SearchResponse searchResponse;


    public static BulkProcessor bulkProcessor;

    public static Logger log = LoggerFactory.getLogger(EsUtil.class);

    public static void init(ESConfigFactory esConfigFactory) throws ConfigurationException {
        String strServers = esConfigFactory.getServers();

        String[] lstServers = strServers.split(",");

        int len = lstServers.length;

        HttpHost[] hostArray = new HttpHost[len];

        for (int i = 0; i < len; i++) {
            String[] serverPorts = lstServers[i].split(":");
            if (serverPorts.length != 2) {
                log.error("Error Parsing ES Server Config: " + strServers);
                throw new ConfigurationException("Error Parsing ES Server Config: " + strServers);
            }
            hostArray[i] = new HttpHost(serverPorts[0].trim(), Integer.parseInt(serverPorts[1].trim()));
        }


        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(esConfigFactory.getUser(), esConfigFactory.getPass()));

        RestClientBuilder builder = RestClient.builder(hostArray)
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        s_HighClient = new RestHighLevelClient(RestClient.builder(hostArray).setHttpClientConfigCallback(httpAsyncClientBuilder ->
                httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)));

        createClient();

        createBulkProcessor();
    }

    public static void createClient(){
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", "jmw_es")
                    .put("client.transport.sniff", Boolean.TRUE)
                    .put("client.transport.ignore_cluster_name", Boolean.FALSE)
                    .put("client.transport.ping_timeout", "5s")
                    .put("client.transport.nodes_sampler_interval", "5s").build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void closeClient() {
        try {
            s_LowClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createBulkProcessor() {
        ThreadPool threadPool = new ThreadPool(Settings.EMPTY);
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            public void beforeBulk(long executionId, BulkRequest request) {

                int numberOfActions = request.numberOfActions();
                log.info("Executing bulk [{}] with {} requests", executionId, numberOfActions);

            }

            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                //  log.info("插入ES成功:  " + response.buildFailureMessage());
                if (!"failure in bulk execution:".equals(response.buildFailureMessage())) {
                    log.error("批量插入error： " + response.buildFailureMessage());
                }

                if (response.hasFailures()) {
                    log.error("Bulk [{}] 插入  failures", executionId);
                } else {
                    log.info("Bulk [{}] completed in {} milliseconds", executionId, response.getTook().getMillis());
                }
            }

            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                log.error("提交失败failure:" + "------>" + failure.getMessage());
            }
        };
        try {
            bulkProcessor = new BulkProcessor.Builder(s_HighClient::bulkAsync, listener,threadPool)
                    .setBulkActions(100)
                    .setFlushInterval(TimeValue.timeValueSeconds(10L))
                    .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(60L), 3))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 缓存消防机构的数据
     */
    public static void searchTest() {
        try {
            searchRequest = new SearchRequest("hockey");
            searchRequest.types("player");
            sourceBuilder = new SearchSourceBuilder();
            QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
            sourceBuilder.query(queryBuilder).size(200).scriptField("born_year",
                    new Script(ScriptType.INLINE,
                            "painless",
                            "doc.born.date.year",
                            Collections.emptyMap()));
            System.out.println(sourceBuilder.toString());
            System.out.println(searchRequest.toString());
            searchRequest.source(sourceBuilder);
            searchResponse = s_HighClient.search(searchRequest);

            SearchHits searchHit = searchResponse.getHits();
            SearchHit[] searchHits = searchHit.getHits();
            for (SearchHit hit : searchHits) {
                System.out.println(hit.getId()+"::"+hit.getField("born_year").getValue().toString());
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

        }
    }

    public static void updateTest(){
        Map<String,Object> params = new HashMap() {};
        params.put("first","55555555");
        params.put("full","66666666");
        UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        updateByQuery.source("hockey").script(
                new Script(
                        ScriptType.INLINE,
                        "painless",
                        "if(ctx._source.last == 'backlund'){ctx._source.first=params.first;ctx._source.full=params.full}",
                        params))
                .source().setTypes("player")
        .setQuery(QueryBuilders.matchAllQuery())
        .setSize(200)
        .setExplain(false);
        System.out.println(updateByQuery.toString());
        BulkByScrollResponse response = updateByQuery.get();
        System.out.println(response.getUpdated());
    }

    public static void testScript() {
//            searchTest();
        updateTest();
    }
}
