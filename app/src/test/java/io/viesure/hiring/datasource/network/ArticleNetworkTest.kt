package io.viesure.hiring.datasource.network

import BaseUnitTest
import com.squareup.moshi.JsonDataException
import io.viesure.hiring.datasource.network.api.ArticleApi
import io.viesure.hiring.datasource.network.dto.ArticleDto
import io.viesure.hiring.di.DiConfig
import io.viesure.hiring.di.initKodein
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
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
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*


@ExperimentalCoroutinesApi
class ArticleNetworkTest : BaseUnitTest() {
    private lateinit var server: MockWebServer
    private lateinit var articleApiInTest: ArticleApi

    @Before
    override fun before() {
        super.before()
        server = MockWebServer()
        server.start()
        val baseUrl = server.url("/").toString()

        val kodein = Kodein {
            initKodein()

            bind<String>(DiConfig.TAG_BASE_URL, true) with singleton { baseUrl }

            bind<HttpLoggingInterceptor.Level>(overrides = true) with singleton { HttpLoggingInterceptor.Level.NONE }
        }

        articleApiInTest = kodein.direct.instance()
    }

    @After
    override fun after() {
        super.after()
        server.shutdown()
    }

    @Test
    fun testSuccess() {
        server.enqueue(MockResponse().setBody(readFile("article_list_valid.json")))

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
            assertEquals(expected, articleApiInTest.getArticles())
        }
    }

    @Test(expected = JsonDataException::class)
    fun testParseError() {
        server.enqueue(MockResponse().setBody(readFile("article_list_invalid.json")))
        runBlocking {
            articleApiInTest.getArticles()
        }
    }


    private fun readFile(filename: String): String {
        val br = BufferedReader(InputStreamReader(FileInputStream("src/test/res/$filename")))
        val sb = StringBuilder()
        var line: String? = br.readLine()
        while (line != null) {
            sb.append(line)
            line = br.readLine()
        }
        return sb.toString()
    }
}