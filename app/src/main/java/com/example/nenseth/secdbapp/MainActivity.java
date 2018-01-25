package com.example.nenseth.secdbapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.IDESFireEV1;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String m_strKey = "05176190715f78ba80d0beebaf09daa3";
    private NxpNfcLib m_libInstance = null;

    private void initializeLibrary() {
        m_libInstance = NxpNfcLib.getInstance();
        m_libInstance.registerActivity(this, m_strKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initializeLibrary();

    }

    @Override
    public void onNewIntent(Intent intent) {
        cardLogic(intent);
        super.onNewIntent(intent);
    }

    private void cardLogic(Intent intent) {
        CardType cardType = m_libInstance.getCardType(intent);
        Log.d(TAG, "cardLogic: cardtype found :" + cardType.getTagName());

        if (CardType.DESFireEV1 == cardType) {
            IDESFireEV1 objDESFireEV1 = DESFireFactory.getInstance().getDESFire(m_libInstance.getCustomModules());
            try
            {
                objDESFireEV1.getReader().connect();
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
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

    @Override
    protected void onResume() {
        m_libInstance.startForeGroundDispatch();
        super.onResume();
    }

    @Override
    protected void onPause() {
        m_libInstance.stopForeGroundDispatch();
        super.onPause();
    }
}
