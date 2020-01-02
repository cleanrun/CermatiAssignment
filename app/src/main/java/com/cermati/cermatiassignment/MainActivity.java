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

import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// Name : Muhammad Marchell

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";

    // view properties
    private RecyclerView recyclerView;
    private EditText etSearch;
    private CardView cvSearch;
    private LinearLayout llNoData;

    private ListAdapter adapter;

    private CompositeDisposable disposables = new CompositeDisposable();

    private ListAdapter.ClickListener clickListener = new ListAdapter.ClickListener() {
        @Override
        public void onListClick(Model model) {
            String message = "ID : " + model.getId() + "\n" + "URL : " + model.getHtml_url();
            showToast(message);
        }
    };

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
        editTextWatcher();

    }

    // recycler view initialization
    private void initRecyclerView(){
        adapter = new ListAdapter(this, clickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // show no data logo if no data is available
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

    // textwatcher fo edittext using rxbinding
    private void editTextWatcher(){
        RxTextView.textChanges(etSearch)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: etwatcher");
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        Log.d(TAG, "onNext: etwacther " + charSequence);
                        getSearchObserver(charSequence.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: etwatcher " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: etwatcher");
                    }
                });
    }

    // getting the user list from github's api
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

    // execute searching function if the button is clicked
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cv_search){
            Log.d(TAG, "onClick: search button called");
            String query = etSearch.getText().toString();

            getSearchObserver(query);
        }
    }

    // emitting the users
    public void getSearchObserver(String query){
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
                        if(e.getMessage().equals("HTTP 422 Unprocessable Entity")){
                            Log.d(TAG, "onError: HTTP 422");
                            // do nothing
                        }else{
                            showToast("Something wrong just happened :(");
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: called");
                    }
                });
    }

    // showing toast
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
}
