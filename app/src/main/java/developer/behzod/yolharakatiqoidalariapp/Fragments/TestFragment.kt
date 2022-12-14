package developer.behzod.yolharakatiqoidalariapp.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import developer.behzod.yolharakatiqoidalariapp.Adapters.CustomAdapter
import developer.behzod.yolharakatiqoidalariapp.Adapters.RvAdapter
import developer.behzod.yolharakatiqoidalariapp.DataBase.MyDbHelper
import developer.behzod.yolharakatiqoidalariapp.Models.Qoyda
import developer.behzod.yolharakatiqoidalariapp.Models.User
import developer.behzod.yolharakatiqoidalariapp.R
import developer.behzod.yolharakatiqoidalariapp.databinding.ItemTalabaOchirishBinding



import kotlinx.android.synthetic.main.fragment_test.view.*
import kotlinx.android.synthetic.main.item_birinchi_vazifa.view.*
import kotlinx.android.synthetic.main.item_second_task.view.*
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime


private const val ARG_PARAM1 = "user"


class TestFragment : Fragment() {

    private var param1: User? = null
    lateinit var root: View
    lateinit var myDbHelper: MyDbHelper
    lateinit var rvAdapter: RvAdapter
    lateinit var customAdapter: CustomAdapter
    lateinit var dRasim: ImageView
    var uris: Uri? = null
    var a: Int? = null
    var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as User?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        root = inflater.inflate(R.layout.fragment_test, container, false)

        updata()


        when (param1!!.nomi) {
            "Ogohlantiruvchi" -> {
                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Ogohlantiruvchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {
                        showF(qoyda.rasim)
                    }

                    override fun editQoyda(qoyda: Qoyda) {
                        val alertDialog = AlertDialog.Builder(root.context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.item_birinchi_vazifa, null, false)
                        dialog.setView(dialogView)
                        dialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        dialog.setCancelable(true)
                        val file = File("${qoyda.rasim.toString()}")
                        val uri = Uri.fromFile(file)
                        dialogView.o_rasim_qoy.setImageURI(uri)
                        val oYolnomi = dialogView.o_yolNomi
                        val oYoltoliqmalumot = dialogView.o_yolToliqMalumot
                        oYolnomi.setText(qoyda.qoydaNomi)
                        oYoltoliqmalumot.setText(qoyda.qoydaToliqMalumot)

                        dRasim = dialogView.o_rasim_qoy


                        customAdapter = CustomAdapter(
                            root.context,
                            arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Taqiqlovchi", "Buyuruvchi")
                        )
                        dialogView.o_spiner!!.adapter = customAdapter

                        dialog.show()

                        dialogView.o_rasim_qoy.setOnClickListener {
                            startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                }, 1
                            )
                        }

                        dialogView.o_saqlash.setOnClickListener {
                            val text1 = dialogView.o_yolNomi.text.toString()
                            val text2 = dialogView.o_yolToliqMalumot.text.toString()

                            val spinnerMentorlar23 = dialogView.o_spiner

                            spinnerMentorlar23.onItemSelectedListener =
                                object : AdapterView.OnItemClickListener,
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemClick(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position


                                        when (a) {
                                            0 -> text = "Ogohlantiruvchi"
                                            1 -> text = "Imtiyozli"

                                            2 -> text = "Taqiqlovchi"

                                            3 -> text = "Buyuruvchi"


                                        }

                                    }

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position


                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {

                                    }

                                }





                            when (a) {
                                0 -> text = "Ogohlantiruvchi"
                                1 -> text = "Imtiyozli"

                                2 -> text = "Taqiqlovchi"

                                3 -> text = "Buyuruvchi"


                            }
                            onResume()


                            myDbHelper = MyDbHelper(root.context)
                            qoyda.qoydaNomi = text1
                            qoyda.qoydaToliqMalumot = text2
                            qoyda.like = "0"
                            qoyda.rasim = uris.toString()
                            qoyda.yolanishi = text



                            myDbHelper.editQoydalar(qoyda)

                            Toast.makeText(root.context, "Malumot Saqlandi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()


                        }

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {
                        myDbHelper = MyDbHelper(root.context)

                        /*  if (qoyda.like == "1") {
                              qoyda.like = "0"
                          } else if (qoyda.like == "0") {
                              qoyda.like = "1"
                          }*/



                        myDbHelper.editQoydalar(qoyda)
                    }

                })


                onResume()
                root.rv.adapter = rvAdapter


            }
            "Imtiyozli" -> {

                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Imtiyozli") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {
                        showF(qoyda.rasim)
                    }

                    override fun editQoyda(qoyda: Qoyda) {
                        val alertDialog = AlertDialog.Builder(root.context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.item_birinchi_vazifa, null, false)
                        dialog.setView(dialogView)
                        dialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        dialog.setCancelable(true)
                        val file = File("${qoyda.rasim.toString()}")
                        val uri = Uri.fromFile(file)
                        dialogView.o_rasim_qoy.setImageURI(uri)
                        val oYolnomi = dialogView.o_yolNomi
                        val oYoltoliqmalumot = dialogView.o_yolToliqMalumot
                        oYolnomi.setText(qoyda.qoydaNomi)
                        oYoltoliqmalumot.setText(qoyda.qoydaToliqMalumot)

                        dRasim = dialogView.o_rasim_qoy


                        customAdapter = CustomAdapter(
                            root.context,
                            arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Taqiqlovchi", "Buyuruvchi")
                        )
                        dialogView.o_spiner!!.adapter = customAdapter

                        dialog.show()

                        dialogView.o_rasim_qoy.setOnClickListener {
                            startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                }, 1
                            )
                        }

                        dialogView.o_saqlash.setOnClickListener {
                            val text1 = dialogView.o_yolNomi.text.toString()
                            val text2 = dialogView.o_yolToliqMalumot.text.toString()

                            val spinnerMentorlar23 = dialogView.o_spiner

                            spinnerMentorlar23.onItemSelectedListener =
                                object : AdapterView.OnItemClickListener,
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemClick(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position


                                        when (a) {
                                            0 -> text = "Ogohlantiruvchi"
                                            1 -> text = "Imtiyozli"

                                            2 -> text = "Taqiqlovchi"

                                            3 -> text = "Buyuruvchi"


                                        }

                                    }

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position


                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {

                                    }

                                }





                            when (a) {
                                0 -> text = "Ogohlantiruvchi"
                                1 -> text = "Imtiyozli"

                                2 -> text = "Taqiqlovchi"

                                3 -> text = "Buyuruvchi"


                            }
                            onResume()


                            myDbHelper = MyDbHelper(root.context)
                            qoyda.qoydaNomi = text1
                            qoyda.qoydaToliqMalumot = text2
                            qoyda.like = "0"
                            qoyda.rasim = uris.toString()
                            qoyda.yolanishi = text



                            myDbHelper.editQoydalar(qoyda)

                            Toast.makeText(root.context, "Malumot Saqlandi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()


                        }

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }

                })

                root.rv.adapter = rvAdapter

                onResume()

            }
            "Taqiqlovchi" -> {

                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Taqiqlovchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {
                        showF(qoyda.rasim)
                    }

                    override fun editQoyda(qoyda: Qoyda) {
                        val alertDialog = AlertDialog.Builder(root.context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.item_birinchi_vazifa, null, false)
                        dialog.setView(dialogView)
                        dialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        dialog.setCancelable(true)
                        val file = File("${qoyda.rasim.toString()}")
                        val uri = Uri.fromFile(file)
                        dialogView.o_rasim_qoy.setImageURI(uri)
                        val oYolnomi = dialogView.o_yolNomi
                        val oYoltoliqmalumot = dialogView.o_yolToliqMalumot
                        oYolnomi.setText(qoyda.qoydaNomi)
                        oYoltoliqmalumot.setText(qoyda.qoydaToliqMalumot)

                        dRasim = dialogView.o_rasim_qoy


                        customAdapter = CustomAdapter(
                            root.context,
                            arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Taqiqlovchi", "Buyuruvchi")
                        )
                        dialogView.o_spiner!!.adapter = customAdapter

                        dialog.show()

                        dialogView.o_rasim_qoy.setOnClickListener {
                            startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                }, 1
                            )
                        }

                        dialogView.o_saqlash.setOnClickListener {
                            val text1 = dialogView.o_yolNomi.text.toString()
                            val text2 = dialogView.o_yolToliqMalumot.text.toString()

                            val spinnerMentorlar23 = dialogView.o_spiner

                            spinnerMentorlar23.onItemSelectedListener =
                                object : AdapterView.OnItemClickListener,
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemClick(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position


                                        when (a) {
                                            0 -> text = "Ogohlantiruvchi"
                                            1 -> text = "Imtiyozli"

                                            2 -> text = "Taqiqlovchi"

                                            3 -> text = "Buyuruvchi"


                                        }

                                    }

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position


                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {

                                    }

                                }





                            when (a) {
                                0 -> text = "Ogohlantiruvchi"
                                1 -> text = "Imtiyozli"

                                2 -> text = "Taqiqlovchi"

                                3 -> text = "Buyuruvchi"


                            }
                            onResume()


                            myDbHelper = MyDbHelper(root.context)
                            qoyda.qoydaNomi = text1
                            qoyda.qoydaToliqMalumot = text2
                            qoyda.like = "0"
                            qoyda.rasim = uris.toString()
                            qoyda.yolanishi = text



                            myDbHelper.editQoydalar(qoyda)

                            Toast.makeText(root.context, "Malumot Saqlandi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()


                        }

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }

                })

                root.rv.adapter = rvAdapter

                onResume()

            }
            "Buyuruvchi" -> {
                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Buyuruvchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {
                        showF(qoyda.rasim)
                    }

                    override fun editQoyda(qoyda: Qoyda) {

                        val alertDialog = AlertDialog.Builder(root.context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.item_birinchi_vazifa, null, false)
                        dialog.setView(dialogView)
                        dialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        dialog.setCancelable(true)
                        val file = File("${qoyda.rasim.toString()}")
                        val uri = Uri.fromFile(file)
                        dialogView.o_rasim_qoy.setImageURI(uri)
                        val oYolnomi = dialogView.o_yolNomi
                        val oYoltoliqmalumot = dialogView.o_yolToliqMalumot
                        oYolnomi.setText(qoyda.qoydaNomi)
                        oYoltoliqmalumot.setText(qoyda.qoydaToliqMalumot)

                        dRasim = dialogView.o_rasim_qoy


                        customAdapter = CustomAdapter(
                            root.context,
                            arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Taqiqlovchi", "Buyuruvchi")
                        )
                        dialogView.o_spiner!!.adapter = customAdapter

                        dialog.show()

                        dialogView.o_rasim_qoy.setOnClickListener {
                            startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                }, 1
                            )
                        }

                        dialogView.o_saqlash.setOnClickListener {
                            val text1 = dialogView.o_yolNomi.text.toString()
                            val text2 = dialogView.o_yolToliqMalumot.text.toString()

                            val spinnerMentorlar23 = dialogView.o_spiner

                            spinnerMentorlar23.onItemSelectedListener =
                                object : AdapterView.OnItemClickListener,
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemClick(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position


                                        when (a) {
                                            0 -> text = "Ogohlantiruvchi"
                                            1 -> text = "Imtiyozli"

                                            2 -> text = "Taqiqlovchi"

                                            3 -> text = "Buyuruvchi"


                                        }

                                    }

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position


                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {

                                    }

                                }





                            when (a) {
                                0 -> text = "Ogohlantiruvchi"
                                1 -> text = "Imtiyozli"

                                2 -> text = "Taqiqlovchi"

                                3 -> text = "Buyuruvchi"


                            }
                            onResume()


                            myDbHelper = MyDbHelper(root.context)
                            qoyda.qoydaNomi = text1
                            qoyda.qoydaToliqMalumot = text2
                            qoyda.like = "0"
                            qoyda.rasim = uris.toString()
                            qoyda.yolanishi = text



                            myDbHelper.editQoydalar(qoyda)

                            Toast.makeText(root.context, "Malumot Saqlandi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()


                        }
                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }

                })

                root.rv.adapter = rvAdapter

                onResume()

            }
        }


        return root
    }

    private fun updata() {
        when (param1!!.nomi) {
            "Ogohlantiruvchi" -> {
                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Ogohlantiruvchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {
                        showF(qoyda.rasim)
                    }

                    override fun editQoyda(qoyda: Qoyda) {
                        val alertDialog = AlertDialog.Builder(root.context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.item_birinchi_vazifa, null, false)
                        dialog.setView(dialogView)
                        dialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        dialog.setCancelable(true)
                        val file = File("${qoyda.rasim.toString()}")
                        val uri = Uri.fromFile(file)
                        dialogView.o_rasim_qoy.setImageURI(uri)
                        val oYolnomi = dialogView.o_yolNomi
                        val oYoltoliqmalumot = dialogView.o_yolToliqMalumot
                        oYolnomi.setText(qoyda.qoydaNomi)
                        oYoltoliqmalumot.setText(qoyda.qoydaToliqMalumot)

                        dRasim = dialogView.o_rasim_qoy


                        customAdapter = CustomAdapter(
                            root.context,
                            arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Taqiqlovchi", "Buyuruvchi")
                        )
                        dialogView.o_spiner!!.adapter = customAdapter

                        dialog.show()

                        dialogView.o_rasim_qoy.setOnClickListener {
                            startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                }, 1
                            )
                        }

                        dialogView.o_saqlash.setOnClickListener {
                            val text1 = dialogView.o_yolNomi.text.toString()
                            val text2 = dialogView.o_yolToliqMalumot.text.toString()

                            val spinnerMentorlar23 = dialogView.o_spiner

                            spinnerMentorlar23.onItemSelectedListener =
                                object : AdapterView.OnItemClickListener,
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemClick(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position

                                        println(a)
                                        when (a) {
                                            0 -> text = "Ogohlantiruvchi"
                                            1 -> text = "Imtiyozli"

                                            2 -> text = "Taqiqlovchi"

                                            3 -> text = "Buyuruvchi"


                                        }

                                    }

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        a = position

                                        println(a)
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {

                                    }

                                }

                            println(a)



                            when (a) {
                                0 -> text = "Ogohlantiruvchi"
                                1 -> text = "Imtiyozli"

                                2 -> text = "Taqiqlovchi"

                                3 -> text = "Buyuruvchi"


                            }
                            onResume()
                            println(text)

                            myDbHelper = MyDbHelper(root.context)
                            qoyda.qoydaNomi = text1
                            qoyda.qoydaToliqMalumot = text2
                            qoyda.like = "0"
                            qoyda.rasim = uris.toString()
                            qoyda.yolanishi = text

                            println(text)


                            myDbHelper.editQoydalar(qoyda)

                            Toast.makeText(root.context, "Malumot Saqlandi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()


                        }
                        onResume()
                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }


                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })



                root.rv.adapter = rvAdapter


            }
            "Imtiyozli" -> {

                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Imtiyozli") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {

                    }

                    override fun editQoyda(qoyda: Qoyda) {


                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })

                root.rv.adapter = rvAdapter


            }
            "Taqiqlovchi" -> {

                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Taqiqlovchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {

                    }

                    override fun editQoyda(qoyda: Qoyda) {

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })

                root.rv.adapter = rvAdapter


            }
            "Buyuruvchi" -> {
                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Buyuruvchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {

                    }

                    override fun editQoyda(qoyda: Qoyda) {

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })

                root.rv.adapter = rvAdapter


            }
        }
    }

    private fun yangilash() {
        myDbHelper = MyDbHelper(root.context)
        val showQoydalar = myDbHelper.showQoydalar()

        when (param1!!.nomi) {
            "Ogohlantiruvchi" -> {
                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Ogohlantiruvchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {

                    }

                    override fun editQoyda(qoyda: Qoyda) {

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {

                    }


                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })



                root.rv.adapter = rvAdapter


            }
            "Imtiyozli" -> {

                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Imtiyozli") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {

                    }

                    override fun editQoyda(qoyda: Qoyda) {


                    }

                    override fun deleteQoyda(qoyda: Qoyda) {

                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })

                root.rv.adapter = rvAdapter


            }
            "Taqiqlovchi" -> {

                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Taqiqlovchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {

                    }

                    override fun editQoyda(qoyda: Qoyda) {

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })

                root.rv.adapter = rvAdapter


            }
            "Buyuruvchi" -> {
                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Buyuruvchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {

                    }

                    override fun editQoyda(qoyda: Qoyda) {

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {

                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })

                root.rv.adapter = rvAdapter


            }
        }

    }

    fun f(qoyda: Qoyda) {
        val dialog = AlertDialog.Builder(root.context).create()
        val itemTalabaOchirishBinding =
            ItemTalabaOchirishBinding.inflate(layoutInflater)
        dialog.setView(itemTalabaOchirishBinding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        itemTalabaOchirishBinding.o.text = "Rostan ham o'chirmoqchimisiz"

        val file = File(qoyda.rasim.toString())
        val uri = Uri.fromFile(file)

        itemTalabaOchirishBinding.rasimOchirish.setImageURI(uri)

        dialog.show()

        itemTalabaOchirishBinding.ha.setOnClickListener {
            myDbHelper = MyDbHelper(root.context)

            myDbHelper = MyDbHelper(root.context)
            myDbHelper.deleteQoydalar(qoyda)

            val file = File(requireActivity().filesDir, "${qoyda.rasim.toString()}")
            for (i in requireActivity().filesDir.listFiles().indices) {
                if (requireActivity().filesDir.listFiles()[i] == file) {
                    requireActivity().filesDir.listFiles()[i].delete()
                    break
                }
            }


            onResume()

            dialog.dismiss()

        }

        itemTalabaOchirishBinding.yoq.setOnClickListener {
            dialog.hide()
        }

        root.rv.adapter = rvAdapter

    }

    override fun onResume() {
        super.onResume()


        when (param1!!.nomi) {
            "Ogohlantiruvchi" -> {
                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Ogohlantiruvchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {
                        showF(qoyda.rasim)
                    }

                    override fun editQoyda(qoyda: Qoyda) {

                        val alertDialog = AlertDialog.Builder(root.context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.item_birinchi_vazifa, null, false)
                        dialog.setView(dialogView)
                        dialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        dialog.setCancelable(true)
                        val file = File("${qoyda.rasim.toString()}")
                        val uri = Uri.fromFile(file)
                        dialogView.o_rasim_qoy.setImageURI(uri)
                        val oYolnomi = dialogView.o_yolNomi
                        val oYoltoliqmalumot = dialogView.o_yolToliqMalumot
                        oYolnomi.setText(qoyda.qoydaNomi)
                        oYoltoliqmalumot.setText(qoyda.qoydaToliqMalumot)

                        dRasim = dialogView.o_rasim_qoy

                        if (qoyda.yolanishi == "Ogohlantiruvchi")
                            a = 0
                        if (qoyda.yolanishi == "Imtiyozli")
                            a = 1
                        if (qoyda.yolanishi == "Taqiqlovchi")
                            a = 2
                        if (qoyda.yolanishi == "Buyuruvchi")
                            a = 3

                        customAdapter = CustomAdapter(
                            root.context,
                            arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Taqiqlovchi", "Buyuruvchi")
                        )
                        dialogView.o_spiner!!.adapter = customAdapter

                        dialog.show()

                        dialogView.o_rasim_qoy.setOnClickListener {
                            startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                }, 1
                            )
                        }


                        val spinnerMentorlar23 = dialogView.o_spiner

                        spinnerMentorlar23.onItemSelectedListener =
                            object : AdapterView.OnItemClickListener,
                                AdapterView.OnItemSelectedListener {
                                override fun onItemClick(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {

                                    a = position

                                    when (a) {
                                        0 -> text = "Ogohlantiruvchi"
                                        1 -> text = "Imtiyozli"
                                        2 -> text = "Taqiqlovchi"
                                        3 -> text = "Buyuruvchi"
                                    }


                                }

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    a = position

                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                }

                            }







                        dialogView.o_saqlash.setOnClickListener {
                            val text1 = dialogView.o_yolNomi.text.toString()
                            val text2 = dialogView.o_yolToliqMalumot.text.toString()
                            var yoji = ""

                            val filesDir = requireActivity().filesDir
                            val listFiles1 = filesDir.listFiles()
                            if (filesDir.isDirectory) {
                                val listFiles = filesDir.listFiles()
                                for (listFile1 in listFiles) {

//listFile1.delete()
                                    yoji = listFile1.toString()
                                }
                            }


                            myDbHelper = MyDbHelper(root.context)
                            qoyda.qoydaNomi = text1
                            qoyda.qoydaToliqMalumot = text2
                            qoyda.like = "0"

                            qoyda.rasim = yoji

                            println(yoji)

                            when (a) {
                                0 -> text = "Ogohlantiruvchi"
                                1 -> text = "Imtiyozli"
                                2 -> text = "Taqiqlovchi"
                                3 -> text = "Buyuruvchi"
                            }

                            println(text)
                            println(a)
                            println(qoyda.qoydaNomi)
                            println(qoyda.qoydaToliqMalumot)


                            qoyda.yolanishi = text


                            myDbHelper.editQoydalar(qoyda)

                            Toast.makeText(root.context, "Malumot Saqlandi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()


                            yangilash()

                        }

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {
                        myDbHelper = MyDbHelper(root.context)

                        qoyda.like = "1"

                        myDbHelper.editQoydalar(qoyda)
                    }
                })



                root.rv.adapter = rvAdapter


            }
            "Imtiyozli" -> {

                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Imtiyozli") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {
                        showF(qoyda.rasim)
                    }

                    override fun editQoyda(qoyda: Qoyda) {
                        val alertDialog = AlertDialog.Builder(root.context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.item_birinchi_vazifa, null, false)
                        dialog.setView(dialogView)
                        dialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        dialog.setCancelable(true)
                        val file = File("${qoyda.rasim.toString()}")
                        val uri = Uri.fromFile(file)
                        dialogView.o_rasim_qoy.setImageURI(uri)
                        val oYolnomi = dialogView.o_yolNomi
                        val oYoltoliqmalumot = dialogView.o_yolToliqMalumot
                        oYolnomi.setText(qoyda.qoydaNomi)
                        oYoltoliqmalumot.setText(qoyda.qoydaToliqMalumot)

                        dRasim = dialogView.o_rasim_qoy

                        if (qoyda.yolanishi == "Ogohlantiruvchi")
                            a = 0
                        if (qoyda.yolanishi == "Imtiyozli")
                            a = 1
                        if (qoyda.yolanishi == "Taqiqlovchi")
                            a = 2
                        if (qoyda.yolanishi == "Buyuruvchi")
                            a = 3

                        customAdapter = CustomAdapter(
                            root.context,
                            arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Taqiqlovchi", "Buyuruvchi")
                        )
                        dialogView.o_spiner!!.adapter = customAdapter

                        dialog.show()

                        dialogView.o_rasim_qoy.setOnClickListener {
                            startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                }, 1
                            )
                        }


                        val spinnerMentorlar23 = dialogView.o_spiner

                        spinnerMentorlar23.onItemSelectedListener =
                            object : AdapterView.OnItemClickListener,
                                AdapterView.OnItemSelectedListener {
                                override fun onItemClick(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {

                                    a = position

                                    when (a) {
                                        0 -> text = "Ogohlantiruvchi"
                                        1 -> text = "Imtiyozli"
                                        2 -> text = "Taqiqlovchi"
                                        3 -> text = "Buyuruvchi"
                                    }


                                }

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    a = position

                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                }

                            }







                        dialogView.o_saqlash.setOnClickListener {
                            val text1 = dialogView.o_yolNomi.text.toString()
                            val text2 = dialogView.o_yolToliqMalumot.text.toString()
                            var yoji = ""

                            val filesDir = requireActivity().filesDir
                            val listFiles1 = filesDir.listFiles()
                            if (filesDir.isDirectory) {
                                val listFiles = filesDir.listFiles()
                                for (listFile1 in listFiles) {

//listFile1.delete()
                                    yoji = listFile1.toString()
                                }
                            }


                            myDbHelper = MyDbHelper(root.context)
                            qoyda.qoydaNomi = text1
                            qoyda.qoydaToliqMalumot = text2
                            qoyda.like = "0"

                            qoyda.rasim = yoji

                            println(yoji)

                            when (a) {
                                0 -> text = "Ogohlantiruvchi"
                                1 -> text = "Imtiyozli"
                                2 -> text = "Taqiqlovchi"
                                3 -> text = "Buyuruvchi"
                            }

                            println(text)
                            println(a)
                            println(qoyda.qoydaNomi)
                            println(qoyda.qoydaToliqMalumot)


                            qoyda.yolanishi = text


                            myDbHelper.editQoydalar(qoyda)

                            Toast.makeText(root.context, "Malumot Saqlandi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()

                            yangilash()

                        }

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })

                root.rv.adapter = rvAdapter


            }
            "Taqiqlovchi" -> {

                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Taqiqlovchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {
                        showF(qoyda.rasim)
                    }

                    override fun editQoyda(qoyda: Qoyda) {
                        val alertDialog = AlertDialog.Builder(root.context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.item_birinchi_vazifa, null, false)
                        dialog.setView(dialogView)
                        dialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        dialog.setCancelable(true)
                        val file = File("${qoyda.rasim.toString()}")
                        val uri = Uri.fromFile(file)
                        dialogView.o_rasim_qoy.setImageURI(uri)
                        val oYolnomi = dialogView.o_yolNomi
                        val oYoltoliqmalumot = dialogView.o_yolToliqMalumot
                        oYolnomi.setText(qoyda.qoydaNomi)
                        oYoltoliqmalumot.setText(qoyda.qoydaToliqMalumot)

                        dRasim = dialogView.o_rasim_qoy

                        if (qoyda.yolanishi == "Ogohlantiruvchi")
                            a = 0
                        if (qoyda.yolanishi == "Imtiyozli")
                            a = 1
                        if (qoyda.yolanishi == "Taqiqlovchi")
                            a = 2
                        if (qoyda.yolanishi == "Buyuruvchi")
                            a = 3

                        customAdapter = CustomAdapter(
                            root.context,
                            arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Taqiqlovchi", "Buyuruvchi")
                        )
                        dialogView.o_spiner!!.adapter = customAdapter

                        dialog.show()

                        dialogView.o_rasim_qoy.setOnClickListener {
                            startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                }, 1
                            )
                        }


                        val spinnerMentorlar23 = dialogView.o_spiner

                        spinnerMentorlar23.onItemSelectedListener =
                            object : AdapterView.OnItemClickListener,
                                AdapterView.OnItemSelectedListener {
                                override fun onItemClick(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {

                                    a = position

                                    when (a) {
                                        0 -> text = "Ogohlantiruvchi"
                                        1 -> text = "Imtiyozli"
                                        2 -> text = "Taqiqlovchi"
                                        3 -> text = "Buyuruvchi"
                                    }


                                }

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    a = position

                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                }

                            }







                        dialogView.o_saqlash.setOnClickListener {
                            val text1 = dialogView.o_yolNomi.text.toString()
                            val text2 = dialogView.o_yolToliqMalumot.text.toString()
                            var yoji = ""

                            val filesDir = requireActivity().filesDir
                            val listFiles1 = filesDir.listFiles()
                            if (filesDir.isDirectory) {
                                val listFiles = filesDir.listFiles()
                                for (listFile1 in listFiles) {

//listFile1.delete()
                                    yoji = listFile1.toString()
                                }
                            }


                            myDbHelper = MyDbHelper(root.context)
                            qoyda.qoydaNomi = text1
                            qoyda.qoydaToliqMalumot = text2
                            qoyda.like = "0"

                            qoyda.rasim = yoji

                            println(yoji)

                            when (a) {
                                0 -> text = "Ogohlantiruvchi"
                                1 -> text = "Imtiyozli"
                                2 -> text = "Taqiqlovchi"
                                3 -> text = "Buyuruvchi"
                            }

                            println(text)
                            println(a)
                            println(qoyda.qoydaNomi)
                            println(qoyda.qoydaToliqMalumot)


                            qoyda.yolanishi = text


                            myDbHelper.editQoydalar(qoyda)

                            Toast.makeText(root.context, "Malumot Saqlandi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()

                            yangilash()

                        }

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {
                        myDbHelper = MyDbHelper(root.context)
                        /*  if (qoyda.like == "1") {
                              qoyda.like = "0"
                          } else if (qoyda.like == "0") {
                              qoyda.like = "1"
                          }*/

                        myDbHelper.editQoydalar(qoyda)

                    }
                })

                root.rv.adapter = rvAdapter


            }
            "Buyuruvchi" -> {
                myDbHelper = MyDbHelper(root.context)
                val showQoydalar = myDbHelper.showQoydalar()
                val newList = ArrayList<Qoyda>()
                for (qoyda in showQoydalar) {
                    if (qoyda.yolanishi == "Buyuruvchi") {

                        newList.add(qoyda)
                    }
                }



                rvAdapter = RvAdapter(root.context, newList, object : RvAdapter.RvClick {
                    override fun onClick(qoyda: Qoyda) {
                        showF(qoyda.rasim)
                    }

                    override fun editQoyda(qoyda: Qoyda) {
                        val alertDialog = AlertDialog.Builder(root.context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.item_birinchi_vazifa, null, false)
                        dialog.setView(dialogView)
                        dialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        dialog.setCancelable(true)
                        val file = File("${qoyda.rasim.toString()}")
                        val uri = Uri.fromFile(file)
                        dialogView.o_rasim_qoy.setImageURI(uri)
                        val oYolnomi = dialogView.o_yolNomi
                        val oYoltoliqmalumot = dialogView.o_yolToliqMalumot
                        oYolnomi.setText(qoyda.qoydaNomi)
                        oYoltoliqmalumot.setText(qoyda.qoydaToliqMalumot)

                        dRasim = dialogView.o_rasim_qoy

                        if (qoyda.yolanishi == "Ogohlantiruvchi")
                            a = 0
                        if (qoyda.yolanishi == "Imtiyozli")
                            a = 1
                        if (qoyda.yolanishi == "Taqiqlovchi")
                            a = 2
                        if (qoyda.yolanishi == "Buyuruvchi")
                            a = 3

                        customAdapter = CustomAdapter(
                            root.context,
                            arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Taqiqlovchi", "Buyuruvchi")
                        )
                        dialogView.o_spiner!!.adapter = customAdapter

                        dialog.show()

                        dialogView.o_rasim_qoy.setOnClickListener {
                            startActivityForResult(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                }, 1
                            )
                        }


                        val spinnerMentorlar23 = dialogView.o_spiner

                        spinnerMentorlar23.onItemSelectedListener =
                            object : AdapterView.OnItemClickListener,
                                AdapterView.OnItemSelectedListener {
                                override fun onItemClick(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {

                                    a = position

                                    when (a) {
                                        0 -> text = "Ogohlantiruvchi"
                                        1 -> text = "Imtiyozli"
                                        2 -> text = "Taqiqlovchi"
                                        3 -> text = "Buyuruvchi"
                                    }


                                }

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    a = position

                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                }

                            }







                        dialogView.o_saqlash.setOnClickListener {
                            val text1 = dialogView.o_yolNomi.text.toString()
                            val text2 = dialogView.o_yolToliqMalumot.text.toString()
                            var yoji = ""

                            val filesDir = requireActivity().filesDir
                            val listFiles1 = filesDir.listFiles()
                            if (filesDir.isDirectory) {
                                val listFiles = filesDir.listFiles()
                                for (listFile1 in listFiles) {

//listFile1.delete()
                                    yoji = listFile1.toString()
                                }
                            }


                            myDbHelper = MyDbHelper(root.context)
                            qoyda.qoydaNomi = text1
                            qoyda.qoydaToliqMalumot = text2
                            qoyda.like = "0"

                            qoyda.rasim = yoji

                            println(yoji)

                            when (a) {
                                0 -> text = "Ogohlantiruvchi"
                                1 -> text = "Imtiyozli"
                                2 -> text = "Taqiqlovchi"
                                3 -> text = "Buyuruvchi"
                            }

                            println(text)
                            println(a)
                            println(qoyda.qoydaNomi)
                            println(qoyda.qoydaToliqMalumot)


                            qoyda.yolanishi = text


                            myDbHelper.editQoydalar(qoyda)

                            Toast.makeText(root.context, "Malumot Saqlandi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()

                            yangilash()

                        }

                    }

                    override fun deleteQoyda(qoyda: Qoyda) {
                        f(qoyda)
                    }

                    override fun likeQoyda(qoyda: Qoyda) {

                    }
                })

                root.rv.adapter = rvAdapter


            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            dRasim.setImageURI(uri)


            val inputStream = requireActivity().contentResolver?.openInputStream(uri)
            val localDateTime = LocalDateTime.now()
            val file = File(requireActivity().filesDir, "$localDateTime images.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream?.close()


        }
    }


    private fun showF(rasim: String?) {
        val alertDialog = AlertDialog.Builder(root.context)
        val dialog = alertDialog.create()

        val dialogView =
            layoutInflater.inflate(R.layout.item_second_task, null, false)
        dialog.setView(dialogView)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        if (rasim != null) {
            val file = File(rasim)
            val uri = Uri.fromFile(file)
            dialogView.second_item_image.setImageURI(uri)
        }
        dialog.setCancelable(true)
        dialog.show()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: User) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }

}
