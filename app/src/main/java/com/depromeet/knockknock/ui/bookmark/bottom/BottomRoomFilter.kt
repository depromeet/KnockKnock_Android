package com.depromeet.knockknock.ui.bookmark.bottom

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.knockknock.R
import com.depromeet.knockknock.ui.bookmark.adapter.FilterRoomAdapter
import com.depromeet.knockknock.ui.bookmark.model.Room
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomRoomFilter(
    val roomList: List<Room>,
    val beforeClickedRoom: List<Int>,
    val callback: (roomList: List<Int>) -> Unit
) : BottomSheetDialogFragment(){
    private lateinit var dlg : BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 이 코드를 실행하지 않으면 XML에서 round 처리를 했어도 적용되지 않는다.
        dlg = ( super.onCreateDialog(savedInstanceState).apply {
            // window?.setDimAmount(0.2f) // Set dim amount here
            setOnShowListener {
                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)

                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.isDraggable = true
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        } ) as BottomSheetDialog
        return dlg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_bottom_room_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clickedRoom: ArrayList<Int> = ArrayList()
        clickedRoom.addAll(beforeClickedRoom)

        for(before in beforeClickedRoom.indices) {
            for (now in roomList.indices) {
                if(beforeClickedRoom[before] == roomList[now].roomId) {
                    roomList[now].isChecked = true
                }
            }
        }

        val roomRecycler = requireView().findViewById<RecyclerView>(R.id.room_recycler)
        val filterAdapter = FilterRoomAdapter { roomId, isChecked ->
            if(isChecked) clickedRoom.add(roomId)
            else clickedRoom.remove(roomId)
        }
        filterAdapter.submitList(roomList)
        roomRecycler.adapter = filterAdapter

        val saveBtn = requireView().findViewById<TextView>(R.id.save_btn)
        saveBtn.setOnClickListener {
            callback.invoke(clickedRoom)
            dismiss()
        }

        val closeBtn = requireView().findViewById<ImageView>(R.id.close_btn)
        closeBtn.setOnClickListener {
            dismiss()
        }
    }
}