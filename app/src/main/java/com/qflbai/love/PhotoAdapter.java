package com.qflbai.love;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @author: qflbai
 * @CreateDate: 2019/10/28 19:03
 * @Version: 1.0
 * @description:
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHodler> {

    private final List<PhotoInfo> mData;
    private final Context mContext;
    private OnItmeClick mOnItmeClick;

    public PhotoAdapter( Context context,List<PhotoInfo > data) {
        mData = data;
        mContext = context;
    }

    @NonNull
    @Override
    public PhotoHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_photo, null, false);

        return new PhotoHodler(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoHodler holder, final int position) {
        PhotoInfo photoInfo = mData.get(position);
       // int mipmapSrc = photoInfo.getMipmapSrc();
        String path = photoInfo.getPath();
        final ImageView photoView = holder.photoView;
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItmeClick!=null){
                    mOnItmeClick.onItmeClick(view,position);
                }
            }
        });
        holder.photoView.setTag(position);
      //  Glide.with(mContext).load(mipmapSrc).placeholder(R.drawable.a1).into(holder.photoView);
        Glide.with(mContext).load(path).placeholder(R.mipmap.ic_launcher).into(photoView);
    }


    //改变bitmap尺寸的方法
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class PhotoHodler extends RecyclerView.ViewHolder{

        public ImageView photoView;

        public PhotoHodler(@NonNull View itemView) {
            super(itemView);

            photoView = itemView.findViewById(R.id.photo_view);
        }


    }

    interface OnItmeClick{
       void onItmeClick(View view,int position);
    }

    public void setOnItemClick(OnItmeClick onItemClick){
        mOnItmeClick = onItemClick;
    }
}
