package io.viesure.hiring.datasource.network

import BaseUnitTest
import com.squareup.moshi.JsonDataException
import io.viesure.hiring.datasource.network.api.ArticleApi
import io.viesure.hiring.datasource.network.dto.ArticleDto
import io.viesure.hiring.di.DiConfig
import io.viesure.hiring.di.initKodein
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.*


@ExperimentalCoroutinesApi
class ArticleNetworkTest : BaseUnitTest() {
    lateinit var server: MockWebServer
    lateinit var testKodein: Kodein

    @Before
    override fun before() {
        super.before()
        server = MockWebServer()
        server.start()
        val baseUrl = server.url("/").toString()

        testKodein = Kodein {
            initKodein()

            bind<String>(DiConfig.TAG_BASE_URL, true) with singleton { baseUrl }

            bind<HttpLoggingInterceptor.Level>(overrides = true) with singleton { HttpLoggingInterceptor.Level.NONE }
        }
    }

    @After
    override fun after() {
        super.after()
        server.shutdown()
    }

    @Test
    fun testSuccess() {
        server.enqueue(
            MockResponse().setBody(
                "[\n" +
                        "{\n" +
                        "\"id\": 1,\n" +
                        "\"title\": \"Realigned multimedia framework\",\n" +
                        "\"description\": \"nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut massa volutpat convallis morbi odio odio elementum eu interdum eu tincidunt in leo maecenas pulvinar lobortis est phasellus sit amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula\",\n" +
                        "\"author\": \"sfolley0@nhs.uk\",\n" +
                        "\"release_date\": \"6/25/2018\",\n" +
                        "\"image\": \"http://dummyimage.com/366x582.png/5fa2dd/ffffff\"\n" +
                        "},\n" +
                        "{\n" +
                        "\"id\": 2,\n" +
                        "\"title\": \"Versatile 6th generation definition\",\n" +
                        "\"description\": \"a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac diam cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in\",\n" +
                        "\"author\": \"ndanet1@sohu.com\",\n" +
                        "\"release_date\": \"9/28/2019\",\n" +
                        "\"image\": \"http://dummyimage.com/573x684.bmp/cc0000/ffffff\"\n" +
                        "}]"
            )
        )

        val expected = listOf(
            ArticleDto(
                "1",
                "Realigned multimedia framework",
                "nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut massa volutpat convallis morbi odio odio elementum eu interdum eu tincidunt in leo maecenas pulvinar lobortis est phasellus sit amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula",
                "sfolley0@nhs.uk",
                Calendar.getInstance().also { it.clear(); it.set(2018, 5, 25) }.time,
                "http://dummyimage.com/366x582.png/5fa2dd/ffffff"
            ),
            ArticleDto(
                "2",
                "Versatile 6th generation definition",
                "a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac diam cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in",
                "ndanet1@sohu.com",
                Calendar.getInstance().also { it.clear(); it.set(2019, 8, 28) }.time,
                "http://dummyimage.com/573x684.bmp/cc0000/ffffff"
            )
        )

        runBlocking {

            val articleApi: ArticleApi = testKodein.direct.instance()

            val actual = articleApi.getArticles()

            assertEquals(expected, actual)
        }


    }

    @Test(expected = JsonDataException::class)
    fun testParseError() {
        server.enqueue(
            MockResponse().setBody(
                "[\n" +
                        "{\n" +
                        "\"id\": 1,\n" +
                        "\"title\": \"Realigned multimedia framework\",\n" +
                        "\"description\": \"nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut massa volutpat convallis morbi odio odio elementum eu interdum eu tincidunt in leo maecenas pulvinar lobortis est phasellus sit amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula\",\n" +
                        "\"author\": \"sfolley0@nhs.uk\",\n" +
                        "\"release_date\": \" gf 6/25/2018 z+12\",\n" +
                        "\"image\": \"http://dummyimage.com/366x582.png/5fa2dd/ffffff\"\n" +
                        "},\n" +
                        "{\n" +
                        "\"id\": 2,\n" +
                        "\"title\": \"Versatile 6th generation definition\",\n" +
                        "\"description\": \"a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac diam cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in\",\n" +
                        "\"author\": \"ndanet1@sohu.com\",\n" +
                        "\"release_date\": \"9/28/2019\",\n" +
                        "\"image\": \"http://dummyimage.com/573x684.bmp/cc0000/ffffff\"\n" +
                        "}]"
            )
        )

        runBlocking {
            val articleApi: ArticleApi = testKodein.direct.instance()
            articleApi.getArticles()
        }
    }
}