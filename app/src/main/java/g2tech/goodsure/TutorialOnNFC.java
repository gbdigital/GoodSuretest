package g2tech.goodsure;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TutorialOnNFC extends Activity {
    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.examples);
        
        Button btag = (Button)findViewById(R.id.buttontag);
		btag.setOnClickListener(new OnClickListener(){
			// @Override
			public void onClick(View arg0) {
				startActivity(new Intent(TutorialOnNFC.this, TagDispatch.class));	
			}
		});
		
		//Button bbeam = (Button)findViewById(R.id.buttonbeam);
		//bbeam.setOnClickListener(new OnClickListener(){
			// @Override
		//	public void onClick(View arg0) {
		//		startActivity(new Intent(TutorialOnNFC.this, BeamData.class));
		//	}
		//});
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
	
}
