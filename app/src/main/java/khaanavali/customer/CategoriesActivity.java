package khaanavali.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import khaanavali.customer.adapter.LocationAdapter;

public class CategoriesActivity extends AppCompatActivity {
    String[] area={"South Indian","North Indian","Punjabi","Chinees"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        LocationAdapter dataAdapter = new LocationAdapter(CategoriesActivity.this,
                R.layout.area_list,area,null);
        ListView listView = (ListView) findViewById(R.id.sub_product_listView);
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(CategoriesActivity.this, ProductDetailViewActivity.class);

                startActivity(i);
            }
        });
    }
}
