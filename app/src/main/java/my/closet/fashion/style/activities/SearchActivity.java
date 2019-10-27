package my.closet.fashion.style.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.closet.fashion.style.R;
import my.closet.fashion.style.adapters.FeedsAdapter;
import my.closet.fashion.style.customs.SpacesItemDecoration;
import my.closet.fashion.style.modesl.FeedResponse;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName() ;
    EditText searchtext;
    RecyclerView recyclerView;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysearch);

        context = SearchActivity.this;

        searchtext = (EditText) findViewById(R.id.searchtext);
        recyclerView = (RecyclerView) findViewById(R.id.searchrecycler);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        recyclerView.setHasFixedSize(true);

        Client client = new Client("YFQP26YB2I", "141881247009e9fad92b495bb81f2b97");
        Index index = client.getIndex("post");
        client.initIndex("post");

        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

                Query query = new Query(s.toString())
                        .setAttributesToRetrieve("description")
                        .setHitsPerPage(3);

                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(final JSONObject jsonObject, AlgoliaException e) {

                       // Log.d(TAG,jsonObject.toString());
                        final ArrayList<FeedResponse> feedResponseArrayList = new ArrayList<>();


                        try {
                            JSONArray hits = jsonObject.getJSONArray("hits");


                            for (int i = 0; i < hits.length(); i++) {
                                JSONObject jsonobject = hits.getJSONObject(i);
                                FeedResponse feedResponse = new FeedResponse();
                                feedResponse.setDescription(jsonobject.getString("description"));
                                feedResponse.setImage(jsonobject.getString("image"));
                                feedResponseArrayList.add(feedResponse);
                            }

                            Toast.makeText(SearchActivity.this,feedResponseArrayList.toString(),Toast.LENGTH_LONG).show();

                            FeedsAdapter feedsAdapter = new FeedsAdapter(context,feedResponseArrayList);
                            recyclerView.setAdapter(feedsAdapter);
                            feedsAdapter.notifyDataSetChanged();







                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }






                    }
                });



            }
        });

    }
}
