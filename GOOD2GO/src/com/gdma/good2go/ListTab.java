package com.gdma.good2go;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*public class ListActivityG2G extends Activity {
    /** Called when the activity is first created. 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        TextView textview = new TextView(this);
        textview.setText("This is the List tab");
        setContentView(textview);
    }
}*/

public class ListTab extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, VOLUNTEERING));

      ListView lv = getListView();
      lv.setTextFilterEnabled(true);

      lv.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	// When clicked, show a toast with the TextView text
        	Toast.makeText(getApplicationContext(), ((TextView) view).getText(),Toast.LENGTH_SHORT).show();
        	}
        });
    }
    static final String[] VOLUNTEERING = new String[] {
        "Rotschild Dog Walk", "Dizengoff Horse Ride", "Florentine House Clean"
      };
}