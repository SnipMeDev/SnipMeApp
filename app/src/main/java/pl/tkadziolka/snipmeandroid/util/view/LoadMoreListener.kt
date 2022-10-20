package pl.tkadziolka.snipmeandroid.util.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val NO_ITEM = 0
private const val ZERO_INDEX = 1

class LoadMoreListener(
    private val itemsPerPage: Int = 1,
    private val action: (Int) -> Unit
): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            // Scroll down
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
            val itemsLeft = (lastItem + ZERO_INDEX) % itemsPerPage
            if (lastItem > RecyclerView.NO_POSITION && itemsLeft == NO_ITEM) {
                action(lastItem)
            }
        }
    }
}