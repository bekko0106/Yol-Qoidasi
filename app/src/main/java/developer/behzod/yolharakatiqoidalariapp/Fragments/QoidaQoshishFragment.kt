package developer.behzod.yolharakatiqoidalariapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import developer.behzod.yolharakatiqoidalariapp.R
import kotlinx.android.synthetic.main.fragment_qoida_qoshish.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class QoshishQoydaFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var root:View

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
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_qoida_qoshish, container, false)

        root.orqagaBelgiQoshish.setOnClickListener {
            findNavController().popBackStack()
        }

        return root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QoshishQoydaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}