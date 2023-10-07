package hh.nxloaderrb

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about.*
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity


class AboutActivity : AppCompatActivity() {
    private var url1="https://github.com/DavidBuchanan314/NXLoader"
    private var url2="https://github.com/huhao1987/NXloaderRB"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        var menuarray=resources.getStringArray(R.array.menu)
        bartitle.text=menuarray.get(1)
        slidemenu.setOnClickListener {
            onBackPressed()
        }
        orauthor.setText(getString(R.string.orauthor,url1))
        orauthor.setOnClickListener{
            openurl(url1)
        }
        orauthor2.setText(getString(R.string.orauthor,url2))

        orauthor2.setOnClickListener{
            openurl(url2)
        }
    }
    fun openurl(url:String)
    {
        var uri = Uri.parse(url)
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.data = uri
        startActivity(intent)
    }
}
