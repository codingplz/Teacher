package com.example.mrwen.otherclass;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import okio.Timeout;

/**
 * Created by 60440 on 2017/2/14.
 */

public final class FileRequestBody<T> extends RequestBody {
    private RequestBody requestBody;
    private RetrofitCallbcak<T> callbcak;
    private BufferedSink bufferedSink;

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    public FileRequestBody(RequestBody requestBody, RetrofitCallbcak<T> callbcak) {
        super();
        this.requestBody = requestBody;
        this.callbcak = callbcak;
    }

    @Override
    public long contentLength() throws IOException {
        return super.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if(bufferedSink==null){
            bufferedSink= Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink){
        return new ForwardingSink(sink) {
            long bytesWriten = 0L;
            long contentLength=0L;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source,byteCount);
                if(contentLength==0){
                    contentLength=contentLength();
                }
                bytesWriten+=byteCount;
                callbcak.onLoading(contentLength,bytesWriten);
            }

        };
    }

}
