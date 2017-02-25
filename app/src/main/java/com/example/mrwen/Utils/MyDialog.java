package com.example.mrwen.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mrwen.activity.R;

/**
 * Created by mrwen on 2017/2/9.
 */

public class MyDialog {
    //显示提示框
    public void showAlertDialgo(Context context,String tip){
        new android.support.v7.app.AlertDialog.Builder(context).setTitle("提示")
                .setMessage(tip)
                .setPositiveButton("确认",null)
                .show();
    }

    //弹出多行输入框
    public void showMultiLineInputDialog(Context context, String title,String defaultString, final TextView textView) {

        final EditText inputServer = new EditText(context);
        //inputServer.setSelection(defaultString.length());
        inputServer.setText(defaultString);
        inputServer.setMaxLines(6);
        inputServer.setMinLines(4);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(
                R.drawable.ic_arrow_check).setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(inputServer.getText());
                    }
                });
        builder.show();
    }


    //弹出单行输入框
    public void showSingleLineInputDialog(Context context, String title, String defaultString,final TextView textView) {

        final EditText inputServer = new EditText(context);
        //inputServer.setSelection(defaultString.length());
        inputServer.setText(defaultString);
        inputServer.setMaxLines(1);
        inputServer.setMinLines(1);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(
                R.drawable.ic_arrow_check).setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(inputServer.getText());
                    }
                });
        builder.show();
    }

    //弹出性别选择框
    public void showGenderSelectDialog(Context context, String title, String defaultString,final TextView textView){
        final View view=View.inflate(context,R.layout.gender_select,null);
        RadioButton rb_man=(RadioButton)view.findViewById(R.id.rb_man);
        RadioButton rb_woman=(RadioButton)view.findViewById(R.id.rb_woman);
        final RadioGroup rg_gender=(RadioGroup)view.findViewById(R.id.rg_gender);
        if(defaultString.equals("男"))
            rb_man.setChecked(true);
        else{
            rb_woman.setChecked(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(
                R.drawable.ic_arrow_check).setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    RadioButton selected = (RadioButton)view.findViewById(rg_gender.getCheckedRadioButtonId());
                    textView.setText(selected.getText());
                    }
                });
        builder.show();
    }

    //弹出学期选择框
    public void showSemesterSelectDialog(Context context, String title, String defaultString,final TextView textView){
        final View view=View.inflate(context,R.layout.semester_select,null);
        RadioButton rb_first_semester=(RadioButton)view.findViewById(R.id.rb_first_semester);
        RadioButton rb_second_semester=(RadioButton)view.findViewById(R.id.rb_second_semester);
        final RadioGroup rg_semester=(RadioGroup)view.findViewById(R.id.rg_semester);
        if(defaultString.equals("上学期"))
            rb_first_semester.setChecked(true);
        else{
            rb_second_semester.setChecked(true);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(
                R.drawable.ic_arrow_check).setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RadioButton selected = (RadioButton)view.findViewById(rg_semester.getCheckedRadioButtonId());
                        textView.setText(selected.getText());
                    }
                });
        builder.show();
    }


    //弹出课程必辅选修选择框
    public void showMajorSelectDialog(Context context, String title, String defaultString,final TextView textView){
        final View view=View.inflate(context,R.layout.major_select,null);
        RadioButton rb_major_must=(RadioButton)view.findViewById(R.id.rb_major_must);
        RadioButton rb_major_assist=(RadioButton)view.findViewById(R.id.rb_major_assist);
        RadioButton rb_major_choose=(RadioButton)view.findViewById(R.id.rb_major_choose);
        final RadioGroup rg_major=(RadioGroup)view.findViewById(R.id.rg_major);
        if(defaultString.equals("必修")){
            rb_major_must.setChecked(true);
        }else if(defaultString.equals("辅修")){
            rb_major_assist.setChecked(true);
        }else {
            rb_major_choose.setChecked(true);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(
                R.drawable.ic_arrow_check).setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RadioButton selected = (RadioButton)view.findViewById(rg_major.getCheckedRadioButtonId());
                        textView.setText(selected.getText());
                    }
                });
        builder.show();
    }

    //弹出课时数目选择
    public void showlessonNumberSelectDialog(Context context, String title,final TextView textView){
        final View view=View.inflate(context,R.layout.lesson_number_revise,null);
        final Spinner sp_lessonNumber=(Spinner) view.findViewById(R.id.sp_lesson_number_revise);
        Integer[] lessonNumber=new Integer[]{1,2,3,4,5};
        ArrayAdapter<Integer> lessonAdapter;
        lessonAdapter=new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item,lessonNumber);
        sp_lessonNumber.setAdapter(lessonAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(
                R.drawable.ic_arrow_check).setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(sp_lessonNumber.getSelectedItem().toString());
                    }
                });
        builder.show();
    }
}
