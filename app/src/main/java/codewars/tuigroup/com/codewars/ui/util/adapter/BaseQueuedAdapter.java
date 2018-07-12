package codewars.tuigroup.com.codewars.ui.util.adapter;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.MainThread;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import codewars.tuigroup.com.codewars.di.ActivityScoped;

@ActivityScoped
public abstract class BaseQueuedAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> items = new ArrayList<>();
    private final ArrayDeque<List<T>> pendingUpdates = new ArrayDeque<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ItemsListener itemsListener = null;

    @Override
    public int getItemCount() {
        return items.size();
    }

    @MainThread
    public T getItem(int position) {
        return items.size() > 0 && position >= 0 && position < items.size() ? items.get(position) : null;
    }

    @MainThread
    public List<T> getItems() {
        return items;
    }

    @MainThread
    public boolean hasPendingUpdates() {
        return !pendingUpdates.isEmpty();
    }

    @MainThread
    public List<T> peekLast() {
        return pendingUpdates.isEmpty() ? items : pendingUpdates.peekLast();
    }

    @MainThread
    public void update(final List<T> items) {
        pendingUpdates.add(items);
        if (pendingUpdates.size() == 1) {
            internalUpdate(items);
        }
    }

    @MainThread
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    private void internalUpdate(final List<T> newItems) {
        Thread thread = new Thread(() -> {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(createItemDiffCallback(items, newItems), false);
            handler.post(() -> {
                items.clear();
                items.addAll(newItems);
                result.dispatchUpdatesTo(buildListUpdateCallback(BaseQueuedAdapter.this));
                performItemsUpdated();
                processQueue();
            });
        });
        thread.start();
    }

    protected ListUpdateCallback buildListUpdateCallback(final RecyclerView.Adapter adapter) {
        return new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                adapter.notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                adapter.notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                adapter.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                adapter.notifyItemRangeChanged(position, count, payload);
            }
        };
    }

    @MainThread
    private void processQueue() {
        pendingUpdates.remove();
        if (!pendingUpdates.isEmpty()) {
            if (pendingUpdates.size() > 1) {
                List<T> lastItems = pendingUpdates.peekLast();
                pendingUpdates.clear();
                pendingUpdates.add(lastItems);
            }
            internalUpdate(pendingUpdates.peek());
        }
    }

    public abstract DiffUtil.Callback createItemDiffCallback(List<T> oldItems, List<T> newItems);

    public void setItemsListener(ItemsListener listener) {
        itemsListener = listener;
    }

    private void performItemsUpdated() {
        if (itemsListener != null) {
            itemsListener.onItemsUpdated();
        }
    }

    public interface ItemsListener {

        void onItemsUpdated();
    }
}