package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    EditText etNewTweet;
    String newTweet;
    Button submitTweetButton;
    RestClient client;
    TextView tvCharacterCount;
    int numCharactersLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client= RestApplication.getRestClient(this);

        etNewTweet = (EditText) findViewById(R.id.etNewTweet);
        submitTweetButton= (Button) findViewById(R.id.btnTweet);
        tvCharacterCount = (TextView) findViewById(R.id.tvCharacterCount);

        etNewTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                numCharactersLeft = 280 - etNewTweet.getText().toString().length();
                tvCharacterCount.setText(numCharactersLeft+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        submitTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTweet = etNewTweet.getText().toString();
                tweetSubmit(newTweet);

            }
        });



    }

    public void tweetSubmit(String message) {
      client.sendTweet(message, new JsonHttpResponseHandler(){
          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
              try {
                  Tweet tweet= Tweet.fromJSON(response);

                  // Prepare data intent
                  Intent data = new Intent();
                  // Pass relevant data back as a result
                  data.putExtra("tweet", Parcels.wrap(tweet));
                  // Activity finished ok, return the data


                  setResult(RESULT_OK, data); // set result code and bundle data for response

                  finish();
              } catch (JSONException e) {
                  e.printStackTrace();
              }

          }
      });

    }



}
