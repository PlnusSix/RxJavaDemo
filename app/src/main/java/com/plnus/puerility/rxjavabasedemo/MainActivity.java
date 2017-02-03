package com.plnus.puerility.rxjavabasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Observer<String> mObserver;
    private Subscriber<String> mSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createObserver_Method_1();
        createObserver_Method_2();
        createObservable();
        subscribe();
        subscribeCoreCode();
    }

    /**
     * 创建观察者的第一种方法
     */
    private void createObserver_Method_1() {
        mObserver = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "mObserver onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "mObserver onError()");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "mObserver onNext()");
            }
        };
    }

    /**
     * 创建观察者的第二种方法
     * 因为Subscriber继承自Observer
     * 所以创建Subscriber的对象也是创建观察者
     * 而实质上内部调用的时候还是先将Observer
     * 转化成Subscriber再进行各种操作的
     */
    private void createObserver_Method_2() {
        mSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "mSubscriber onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "mSubscriber onError()");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "mSubscriber onNext()");
            }

            /**
             * 这是Subscriber新增的一个方法
             * 在subscribe刚开始的时候，事件没有被发送之前调用
             * 用于一些准备工作,比如数据清零或者重置
             * 默认情况下实现为空
             * 总是在subscribe发生的线程被调用，不能被指定线程
             * 如果要指定线程的话,可以使用doOnSubscribe()方法
             */
            @Override
            public void onStart() {
                super.onStart();
                Log.e(TAG, "mSubscriber onStart()");
            }
        };
    }

    /**
     * 创建一个被观察者
     */
    private void createObservable() {
        //使用create方式创建一个Observable的对象
        Observable<String> observable_1 = Observable.create(new Observable.OnSubscribe<String>() {

            /**
             * 当Observable被订阅的时候,OnSubscribe的call()方法会自动被调用
             * 事件序列就会依照设定依次触发，对于下面的代码就是调用三次onNext()，然后onCompleted()
             * 这样由被观察者调用了观察者的回调方法
             * 就实现了由被观察者向观察者的事件传递，即
             * 【观察者模式】
             * @param subscriber
             */
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("a");
                subscriber.onNext("b");
                subscriber.onNext("c");
                //数据发送完毕之后调用结束标志
                subscriber.onCompleted();
            }
        });

        //使用just方式
        Observable<String> observable_2 = Observable.just("a", "b", "c");
        /**
         * 将会依次调用：
         * onNext("a");
         * onNext("b");
         * onNext("c");
         * onCompleted();
         */

        //使用from方式
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        Observable<String> observable_3 = Observable.from(list);

        //使用[]数组方式
        String[] values = {"a", "b", "c"};
        Observable<String> observable_4 = Observable.from(values);
    }

    private void subscribe() {
        Observable<String> observable = Observable.just("1");
        //两种观察者，两种订阅模式
        observable.subscribe(mObserver);
        observable.subscribe(mSubscriber);
    }

    private void subscribeCoreCode(){
        /**
         * public Subscription subscribe(Subscriber subscriber) {
         *      subscriber.onStart();
         *      onSubscribe.call(subscriber);
         *      return subscriber;
         * }
         *
         * 将传入的 Subscriber 作为 Subscription 返回。这是为了方便 unsubscribe()
         */
    }
}
