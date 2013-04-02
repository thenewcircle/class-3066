/* $Id: $
   Copyright 2013, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.callmeike.android.sandbox.tagview;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


/**
*
* @version $Revision: $
* @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
*/
public class TagViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_view);

        TagView tags = ((TagView) findViewById(R.id.tags));
        tags.addTag("One", 0);
        tags.addTag("Two", 0);
        tags.addTag("Three", 0);
        tags.addTag("Four", 0);
        tags.addTag("Five", 0);
        tags.addTag("Six", 1);
        tags.addTag("Seven", 0);
        tags.addTag("Eight", 0);
        tags.addTag("Nine", 1);
        tags.addTag("Ten", 0);
        tags.addTag("Eleven", 0);
        tags.addTag("Twelve", 0);
        tags.addTag("Thirteen Twenty-three Thirty-three Forty-three Fifty-three Sixty-three Seventy-three Eighty-three Ninty-three One oh three", 0);
        tags.addTag("Fourteen", 0);
        tags.addTag("Fifteen", 0);
        tags.addTag("Sixteen", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tag_view, menu);
        return true;
    }

}
