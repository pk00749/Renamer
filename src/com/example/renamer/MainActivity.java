package com.example.renamer;
import java.io.File;

import android.app.Activity;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.os.Environment;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;


public class MainActivity extends Activity {
	
	public String sdPath = Environment.getExternalStorageDirectory().getPath();
	public File newFile = new File(sdPath,"aFolder");
	public File riskFile = new File(sdPath,"aBRFolder");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		//---Button Create view---
		Button btnCreate = (Button) findViewById(R.id.btnCreate);
		btnCreate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean isExisted = isSdCardExist();
				if(isExisted) {
					createFolder();
				} else {
					DisplayToast("SD card is not available!");
				}
			}
		});
		
		//---Button Rename view---
		Button btnRename = (Button) findViewById(R.id.btnRename);
		btnRename.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				rename();
			}
		});
		
		//---Button Rename view---
		Button btnAction = (Button) findViewById(R.id.btnAction);
		btnAction.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				copy();
			}
		});
	}
	
	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public void createFolder(){		
		if(!newFile.exists()) {
			boolean isCreated = newFile.mkdir();
			if(isCreated == false){
				DisplayToast("Creation fail");
			} else {
				DisplayToast("Creation successfully");
			}
		} else {
			DisplayToast("Test folder existed!");
		}
		if(!riskFile.exists()) {
			boolean isCreated = riskFile.mkdir();
			if(isCreated == false){
				DisplayToast("Creation fail");
			} else {
				DisplayToast("Creation successfully");
			}
		} else {
			DisplayToast("Risk folder existed!");
		}
	}
	
	public void rename() {
		File[] sourceFileList = newFile.listFiles();	//获得文件对象	
		
		if(sourceFileList != null) {
			for(int i = 0;i < sourceFileList.length;i++) {
				System.out.println(sourceFileList[i]);
				File sourceFile = new File(newFile+File.separator+sourceFileList[i].getName());
				File targetFile = new File(newFile+File.separator+sourceFileList[i].getName()+".bk");
				boolean isRenamed = sourceFile.renameTo(targetFile);
				System.out.println(isRenamed);
			}
			DisplayToast("Renamed successfully!");
		} else {
			DisplayToast("Target folder is empty!");
		}
	}
	
	public void copy() {
		try {
			String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
			File Camera = new File(DCIM,"Camera");
			File[] sourceFileList = Camera.listFiles();	//获得文件对象	
			Arrays.sort(sourceFileList, new Comparator<File>(){
				public int compare(File f1, File f2){
					long diff = f1.lastModified() - f2.lastModified();
					if(diff > 0) return 1;
					else if(diff == 0) return 0;
					else return -1;
				}
				public boolean equals(Object obj){
					return true;
				}
			});
					
			File sourceFile = new File(Camera+File.separator+sourceFileList[sourceFileList.length-1].getName());
			File targetFile = new File(riskFile+File.separator+sourceFileList[sourceFileList.length-1].getName());

			InputStream fosfrom = new FileInputStream(sourceFile);
			OutputStream fosto = new FileOutputStream(targetFile);
			byte bt[] = new byte[1024*1024*5];
			int count; //the number of bytes from buffer to write to this stream.
			while ((count = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, count);		
			}
			fosfrom.close();
			fosto.close();
			DisplayToast("Action successfully!");

		} catch (Exception ex) {
			DisplayToast("Action fail!");
		}
	}
	
	private void DisplayToast(String msg) {
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
	};
}
