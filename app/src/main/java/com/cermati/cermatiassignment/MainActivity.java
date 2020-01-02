package com.cermati.cermatiassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private EditText etSearch;
    private CardView cvSearch;
    private LinearLayout llNoData;

    private ListAdapter adapter;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_search);
        etSearch = findViewById(R.id.et_search);
        cvSearch = findViewById(R.id.cv_search);
        llNoData = findViewById(R.id.ll_no_data);

        cvSearch.setOnClickListener(this);

        initRecyclerView();
        checkData();
    }

    private void initRecyclerView(){
        adapter = new ListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void checkData(){
        if(adapter.getData().isEmpty()){
            llNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        else{
            llNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private Observable<Model> getSearchObservable(String query){
        return ApiClient.getRequestApi()
                .getSearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResponseSearch<Model>, ObservableSource<Model>>() {
                    @Override
                    public ObservableSource<Model> apply(ResponseSearch<Model> modelResponseSearch) throws Exception {
                        adapter.setData(modelResponseSearch.getItems());
                        checkData();
                        return Observable.fromIterable(modelResponseSearch.getItems())
                                .subscribeOn(Schedulers.io());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cv_search){
            Log.d(TAG, "onClick: search button called");
            String query = etSearch.getText().toString();

            getSearchObservable(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Model>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: called");
                        }

                        @Override
                        public void onNext(Model model) {
                            Log.d(TAG, "onNext: called");
                            Log.d(TAG, "Name : " + model.getName() + "\n" + "Image : " + model.getImage() );
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: " + e.getMessage() );
                            showToast("Something wrong just happened :(");
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: called");
                        }
                    });
        }
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
