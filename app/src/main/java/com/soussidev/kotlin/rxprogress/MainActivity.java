package com.soussidev.kotlin.rxprogress;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.soussidev.kotlin.rxprogress.util.AnimationHelper;
import com.soussidev.kotlin.rxprogress.util.AnimationScale;
import com.soussidev.kotlin.rxprogress2.RxProgress;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Flowable<String> user_Flowable;
    private static final long TIME_DELAY = 2000L;
    private CompositeDisposable mCompositeDisposable;
    private Observable<String> user_Observable;
    private AppCompatButton btn_loading_observe,btn_loadig_flowable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("By soussidev");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

        //Set Default Text to Button
       btn_loading_observe.setText("Show Loading Observe");
       btn_loadig_flowable.setText("Show Loading Flowable");

        });

        InitView();

        //Init Observe for btn observe
        user_Observable = Observable
                .timer(TIME_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .doOnTerminate(() -> btn_loading_observe.setText("Observing Again"))
                .doOnComplete(() -> AnimationView()) //if complete show animation
                .map(aLong -> getMessageResult()); //Call function messageresult()

        //Init Flowable for btn Floable
        user_Flowable = Flowable
                .timer(TIME_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .doOnComplete(() -> btn_loadig_flowable.setText("Flowable Again"))
                .doOnComplete(() -> AnimationView()) //if complete show animation
                .map(aLong -> getMessageResult());  //Call function messageresult()

    }
    //Function to show animation group
    private void AnimationView()
    {
        AnimationHelper p =new AnimationHelper(AnimationScale.TransLationTop);
        p.animateGroup(findViewById(R.id.btn_loading_observe),findViewById(R.id.btn_loading_flowable),findViewById(R.id.fab));


    }
     //Function initview()

    public void  InitView()
    {
        //init Composite
        mCompositeDisposable = new CompositeDisposable();
      //init AppCompatButton
     btn_loading_observe=(AppCompatButton)findViewById(R.id.btn_loading_observe);
     btn_loadig_flowable=(AppCompatButton)findViewById(R.id.btn_loading_flowable);

        //btn observe
        btn_loading_observe.setOnClickListener(view->{

            mCompositeDisposable.add(RxProgress.from(MainActivity.this)
                    .withMessage("Logging in...")
                    .forObservable(user_Observable)
                    .subscribe(id -> getMessage(id),
                            throwable -> Log.w(TAG, throwable.getMessage())));

        });

        //btn Flowable
        btn_loadig_flowable.setOnClickListener(view ->{

            mCompositeDisposable.add(RxProgress.from(MainActivity.this)
                    .forFlowable(user_Flowable, BackpressureStrategy.DROP)
                    .subscribe(id -> getMessage(id),
                            throwable -> Log.w(TAG, throwable.getMessage())));

        } );


    }

    //Function to start Snackbar and showing message
    public void getMessage(String name)

    {
        Snackbar.make(btn_loading_observe,name,Snackbar.LENGTH_SHORT).show();
    }

    //Function to get UUID
    private String getMessageResult()
    {
        String res="UUID is " + UUID.randomUUID().toString();
        return res;
    }

    // function show date and time
    private String getDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MMMM,YYYY hh,mm,a");
        return  sdf.format(c.getTime());
    }


    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
