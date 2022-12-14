package developer.behzod.yolharakatiqoidalariapp.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.behzod.yolharakatiqoidalariapp.DataBase.MyDbHelper
import developer.behzod.yolharakatiqoidalariapp.Models.Qoyda
import developer.behzod.yolharakatiqoidalariapp.R
import developer.behzod.yolharakatiqoidalariapp.databinding.ItemRvBinding

import java.io.File


class RvAdapter(val context: Context, val list: List<Qoyda>, var rvClick: RvClick) :
    RecyclerView.Adapter<RvAdapter.VH>() {
    inner class VH(var itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(qoyda: Qoyda) {
            itemRvBinding.nomiQoyda.text = qoyda.qoydaNomi

            val file = File(qoyda.rasim.toString())

            if (qoyda.like == "1")
                itemRvBinding.yurakQoyda.setImageResource(R.drawable.heart)

            val uri = Uri.fromFile(file)

            itemRvBinding.belgiRasim.setImageURI(uri)

            itemRvBinding.belgiRasim.setOnClickListener {
                rvClick.onClick(qoyda)
            }
            itemRvBinding.yurakQoyda.setOnClickListener {

                if (qoyda.like == "0") {
                    itemRvBinding.yurakQoyda.setImageResource(R.drawable.heart)
                    qoyda.like = "1"
                    MyDbHelper(context).editQoydalar(qoyda)
                } else
                    if (qoyda.like == "1") {
                        itemRvBinding.yurakQoyda.setImageResource(R.drawable.like_bosilmaganda)
                        qoyda.like = "0"
                        MyDbHelper(context).editQoydalar(qoyda)
                    }
            }

            itemRvBinding.images.setOnClickListener {
                rvClick.editQoyda(qoyda)
            }
            itemRvBinding.tvName.setOnClickListener {
                rvClick.editQoyda(qoyda)
            }
            itemRvBinding.tvOchirish.setOnClickListener {
                rvClick.deleteQoyda(qoyda)
            }
            itemRvBinding.imagesOchirish.setOnClickListener {
                rvClick.deleteQoyda(qoyda)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface RvClick {
        fun onClick(qoyda: Qoyda)
        fun editQoyda(qoyda: Qoyda)
        fun deleteQoyda(qoyda: Qoyda)
        fun likeQoyda(qoyda: Qoyda)
    }
}