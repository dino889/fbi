package com.example.fbi

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class PlaceListAdapter(
    val context: Context,
    val placeList: ArrayList<PlaceList>
) :
    RecyclerView.Adapter<PlaceListAdapter.Holder>(), Filterable {

    var filteredList: ArrayList<PlaceList>? = null //필터링된 아이템 리스트
    var index : Int = -1 //아이템 배경색 변경 위한 인덱스 저장
    var filter_option : Int = 0

    interface ItemClickListener
    {
        fun onClick(view: View, position: Int)
    }
    //클릭리스너 선언
    private var itemClickListner: ItemClickListener? = null

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    init{
        filteredList = placeList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_place_list_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = filteredList!![position]
        holder?.bind(filteredList!![position], context)

        //아이템 클릭 이벤트
        holder.itemView.setOnClickListener(View.OnClickListener {
            index = position
            notifyDataSetChanged() //변경된 데이터 리스트 적용
            itemClickListner?.onClick(it, position) //GPSFragment의 클릭이벤트 호출
        })

        var color :Int = ContextCompat.getColor(context, R.color.colorSelected)
        //선택된 아이템 처리
        if (index === position) {

            if(holder.itemView.isSelected) {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
                holder.itemView.isSelected = false
            }
            else{
                holder.itemView.setBackgroundColor(color)
                holder.itemView.isSelected = true
            }

        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.itemView.isSelected = false
        }
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val place_title = itemView?.findViewById<TextView>(R.id.tv_rv_place_title)
        val place_address = itemView?.findViewById<TextView>(R.id.tv_rv_place_address)
        val place_distance = itemView?.findViewById<TextView>(R.id.tv_rv_place_distance)

        fun bind (placelist: PlaceList, context: Context) {

            place_title?.text = placelist.title
            place_address?.text = placelist.address
            place_distance?.text = placelist.distance

        }
    }

    fun setOption(option : Int){
        filter_option = option
    }

    override fun getFilter(): Filter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}