package com.example.altafshah.goldbin;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ScrapList_Plastic extends AppCompatActivity {
    public static List<Scrap> scrapList = new ArrayList<>();
    public static List<Scrap> scrapListAddedToCard = new ArrayList<>();
    private RecyclerView recyclerView;
    private ScrapAdapter mAdapter;
    Button addToCart;
    RelativeLayout notificationCount1;
    public static TextView tv;
    Intent innn;
    public static int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrap_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //notificationCount1 = (RelativeLayout) findViewById(R.id.relative_layout1);
        mAdapter = new ScrapAdapter(scrapList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareScrapData();


        addToCart = (Button) findViewById(R.id.add_to_cart_btn);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrapListAddedToCard.clear();

                //int itemInCartPossition = 0;
                for (int i = 0; i<scrapList.size(); i++){

                    int Q =  Integer.parseInt(scrapList.get(i).getQuantity());

                    if (Q>0){
                        scrapListAddedToCard.add(scrapList.get(i));

                    }

                }
                Toast.makeText(getApplicationContext(), ""+count +" Items added to your bag..", Toast.LENGTH_LONG).show();
                ScrapList.tv.setText(""+count);
            }
        });
    }

    private void prepareScrapData() {
        Scrap scrap = new Scrap("Soft Plastic", "200 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Hard Plastic", "100 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Mix Plastic", "60 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Milk Cover", "200 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Water/Oil Cover", "2000 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Polythene(Mix)", "1000 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("HPP", "200 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Plastic Jar 15 ltr", "876 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Fibre", "342 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Plastic Jar 5 ltr", "50 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Polythene(high)", "20 Rs", "Per KG", "0");
        scrapList.add(scrap);

        scrap = new Scrap("Plastic Sack", "900 Rs", "Per KG", "0");
        scrapList.add(scrap);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item1 = menu.findItem(R.id.actionbar_item);
        MenuItemCompat.setActionView(item1, R.layout.saved_in_bag);
        notificationCount1 = (RelativeLayout) MenuItemCompat.getActionView(item1);

        tv = (TextView) notificationCount1.findViewById(R.id.badge_notification_1);
        tv.setText(""+count);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                innn = new Intent(ScrapList_Plastic.this, Checkout.class);
                startActivity(innn);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.actionbar_item)
        {

        }


        return super.onOptionsItemSelected(item);

    }
}
