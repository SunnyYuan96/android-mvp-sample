package com.example.yuansunny.imagesearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.yuansunny.imagesearch.model.Image;
import com.example.yuansunny.imagesearch.presenter.SearchPresenter;
import com.example.yuansunny.imagesearch.util.BaseSchedulerProvider;
import com.example.yuansunny.imagesearch.util.SchedulerProvider;
import com.example.yuansunny.imagesearch.view.ImageAdapter;
import com.example.yuansunny.imagesearch.view.ImageItemDecoration;
import com.example.yuansunny.imagesearch.view.SearchImagesView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchImagesView,
        View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = false;
    private EditText mKeyword;
    private Button mSearchButton;
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private SearchPresenter mSearchPresenter;
    private BaseSchedulerProvider mSchedulerProvider;
    private ProgressBar mProgressBar;
    private boolean mIsShowGrid;
    private int mCurrentPosition;

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!recyclerView.canScrollVertically(1)) {
                mSearchPresenter.loadMoreData(mKeyword.getText().toString());
                if (DEBUG) Log.d(TAG, "Scroll bottom");
            }

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (mIsShowGrid) {
                    StaggeredGridLayoutManager layoutManager =
                            (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                    int[] positions = layoutManager.findFirstCompletelyVisibleItemPositions(null);
                    mCurrentPosition = positions.length > 0 ? positions[0] : 0;
                } else {
                    LinearLayoutManager layoutManager =
                            (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mCurrentPosition = layoutManager.findFirstVisibleItemPosition();
                }
                if (DEBUG) Log.d(TAG, "Current position : " + mCurrentPosition);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKeyword = findViewById(R.id.keyword);
        mSearchButton = findViewById(R.id.search_button);
        mRecyclerView = findViewById(R.id.rv_image_list);
        mProgressBar = findViewById(R.id.progress_bar);
        mSearchButton.setOnClickListener(this);

        mSchedulerProvider = SchedulerProvider.getInstance();
        mSearchPresenter = new SearchPresenter(this, mSchedulerProvider);
        mSearchPresenter.init();

        mAdapter = new ImageAdapter(this);
        setLayoutManager();
        ImageItemDecoration decoration = new ImageItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.grid_margin));
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mKeyword.getWindowToken(), 0);
                mSearchPresenter.searchImages(mKeyword.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_layout_menu_item:
                setLayoutManager();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void updateResult(List<Image> images) {
        if (DEBUG) Log.d(TAG, "Update result");
        mAdapter.setImagesData(images);
    }

    @Override
    public void setProgressShow(boolean isShow) {
        mSearchButton.setClickable(!isShow);
        mProgressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void setLayoutManager() {
        mRecyclerView.stopScroll();
        mIsShowGrid = !mIsShowGrid;
        if (mIsShowGrid) {
            StaggeredGridLayoutManager staggeredGridLayoutMgr =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            staggeredGridLayoutMgr.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            mRecyclerView.setLayoutManager(staggeredGridLayoutMgr);

        } else {
            mRecyclerView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mCurrentPosition);
    }
}
