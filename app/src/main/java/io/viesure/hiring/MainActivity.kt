package io.viesure.hiring

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.viesure.hiring.screen.articlelist.ArticleListNavigator
import io.viesure.hiring.screen.base.Navigator
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity() {
    private val kodein: Kodein by lazy { (applicationContext as KodeinAware).kodein }
    private val navigator: Navigator by lazy { kodein.direct.instance<Navigator>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO add deepLinking support
        if (supportFragmentManager.findFragmentById(R.id.main_fragment_container) == null) {
            navigator.open(this, ArticleListNavigator.uri(), false)
        }
    }

    fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}
