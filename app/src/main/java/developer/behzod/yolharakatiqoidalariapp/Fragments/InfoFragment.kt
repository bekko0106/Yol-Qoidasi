package developer.behzod.yolharakatiqoidalariapp.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import developer.behzod.yolharakatiqoidalariapp.R
import kotlinx.android.synthetic.main.fragment_info.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InfoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        root = inflater.inflate(R.layout.fragment_info, container, false)

        root.telegram.setOnClickListener {

            startUri("https://t.me/Behzod_Madaminov")

        }
        root.instagram.setOnClickListener {

            startUri("https://www.instagram.com/madam1nov.behzod")
        }
        root.facebook.setOnClickListener {

            startUri("https://www.facebook.com/behzod.madaminov.923")
        }
        root.twitter.setOnClickListener {

            startUri("https://twitter.com/Madaminov_bekko")
        }
        root.github.setOnClickListener {

            startUri("https://github.com/bekko0106  ")
        }
        root.linkedin.setOnClickListener {
            startUri("https://www.linkedin.com/in/behzod-madaminov-34254424a/")
        }

        return root
    }

    fun startUri(uri: String) {
        val uris = Uri.parse(uri)
        startActivity(Intent(Intent.ACTION_VIEW, uris))

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}