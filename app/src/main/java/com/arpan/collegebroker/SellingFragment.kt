//package com.arpan.collegebroker
//
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import kotlinx.android.synthetic.main.fragment_selling.*
//import android.support.v7.widget.helper.ItemTouchHelper
//import android.support.design.widget.Snackbar
//import android.graphics.Color
//import android.support.design.widget.FloatingActionButton
//
//class SellingFragment : Fragment(), FlatRecyclerHelper.RecyclerItemTouchHelperListener {
//    val flats = ArrayList<Flat>()
//    var adapter: FlatsAdapter? = null
//
//
//    private lateinit var mainFab: FloatingActionButton
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_selling, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        initRecyclerView()
//
//        mainFab = activity?.findViewById(R.id.mainFab)!!
//
//        mainFab.setOnClickListener {
//            println("Fragment 1 listener")
//        }
//    }
//
//    private fun initRecyclerView() {
////
////        flats.add(Flat("Baner", 0, description = "A lavish flat in Baner", price = 18000))
////        flats.add(Flat("Pashan", 0, description = "Nice and big flat without power cut.", price = 20000))
//
//        adapter = FlatsAdapter(context, flats)
//        flatsRecyclerView.adapter = adapter
//        flatsRecyclerView.layoutManager = LinearLayoutManager(context)
//
//        val itemTouchHelperCallback = FlatRecyclerHelper(0, ItemTouchHelper.LEFT, this)
//        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(flatsRecyclerView)
//
//    }
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
//        if (viewHolder is FlatsAdapter.FlatViewHolder) {
//            // get the removed item location to display it in snack bar
//            val name = flats[viewHolder.adapterPosition].location
//
//            // backup of removed item for undo purpose
//            val deletedItem = flats[viewHolder.adapterPosition]
//            val deletedIndex = viewHolder.adapterPosition
//
//            // remove the item from recycler view
//            adapter?.removeItem(viewHolder.adapterPosition)
//
//            // showing snack bar with Undo option
//            val snackbar = Snackbar
//                    .make(fragment_selling, "$name removed from cart!", Snackbar.LENGTH_LONG)
//            snackbar.setAction("UNDO", {
//                // undo is selected, restore the deleted item
//                adapter?.restoreItem(deletedItem, deletedIndex);
//            })
//            snackbar.setActionTextColor(Color.YELLOW)
//            snackbar.show()
//        }
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance() = SellingFragment()
//    }
//
//}
