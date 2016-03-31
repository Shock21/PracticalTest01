package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private final static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;

    Button left_button = null;
    Button right_button = null;
    Button navigate = null;
    EditText left = null;
    EditText right = null;
    int value_left;
    int value_right;
    boolean serviceStatus = false;

    private IntentFilter intentFilter = new IntentFilter();

    ButtonClickListener listener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_left:
                    value_left++;
                    left.setText(String.valueOf(value_left));
                    if(value_left + value_right > 10 && serviceStatus == false) {
                        Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                        intent.putExtra("firstNumber", value_left);
                        intent.putExtra("secondNumber", value_right);
                        getApplicationContext().startService(intent);
                        serviceStatus = true;
                    }
                    break;
                case R.id.button_right:
                    value_right++;
                    right.setText(String.valueOf(value_right));
                    if(value_left + value_right > 10 && serviceStatus == false) {
                        Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                        intent.putExtra("firstNumber", value_left);
                        intent.putExtra("secondNumber", value_right);
                        getApplicationContext().startService(intent);
                        serviceStatus = true;
                    }
                    break;
                case R.id.button_hide:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                    int numberOfClicks = Integer.parseInt(left.getText().toString()) + Integer.parseInt(right.getText().toString());
                    intent.putExtra("numberOfClicks", numberOfClicks);
                    startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intentFilter.addAction(Intent.ACTION_SEND);

        left_button = (Button)findViewById(R.id.button_left);
        right_button = (Button)findViewById(R.id.button_right);
        left = (EditText)findViewById(R.id.number_left);
        right = (EditText)findViewById(R.id.number_right);

        left.setText("0");
        right.setText("0");

        left_button.setOnClickListener(listener);
        right_button.setOnClickListener(listener);

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey("left_count")) {
                left.setText(savedInstanceState.getString("left_count"));
            } else {
                left.setText("0");
            }
        }

        navigate = (Button)findViewById(R.id.button_hide);
        navigate.setOnClickListener(listener);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("left_count", left.getText().toString());
        savedInstanceState.putString("right_count", right.getText().toString());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState.containsKey("left_count")) {
            left.setText(savedInstanceState.getString("left_count"));
        } else {
            left.setText("0");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "This activity returned with result " + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test01_main, menu);
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
