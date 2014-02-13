package com.mobigeni.cars.motors.parts.search;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.VideoView;

/**
 * This class serves as a WebChromeClient to be set to a WebView, allowing it to play video.
 * It works with old VideoView-s as well as the new HTML5VideoFullScreen inner classes.
 * With VideoView (typically API level <11), it will always show full-screen.
 * With HTML5VideoFullScreen (typically API level 11+), it will show both in-page and full-screen.
 * 
 * IMPORTANT NOTES:
 * - For API level 11+, android:hardwareAccelerated="true" must be set in the application manifest.
 * - The invoking activity must call VideoEnabledWebChromeClient's onBackPressed() inside of its own onBackPressed().
 * - Tested in Android API level 8+. Only tested on http://m.youtube.com.
 *
 * @author Cristian Perez (http://cpr.name)
 *
 */
public class VideoEnabledWebChromeClient extends WebChromeClient implements OnPreparedListener, OnCompletionListener, OnErrorListener
{
    public interface ToggledFullscreenCallback
    {
        public void toggledFullscreen(boolean fullscreen);
    }

    private View activityNonVideoView;
    private ViewGroup activityVideoView;
    private View loadingView;
    private VideoEnabledWebView webView;
private Context c ;
    private boolean isVideoFullscreen; // Indicates if the video is being displayed using a custom view (typically full-screen)
    private FrameLayout videoViewContainer;
    private CustomViewCallback videoViewCallback;

    private ToggledFullscreenCallback toggledFullscreenCallback;

    /**
     * Never use this constructor alone.
     * This constructor allows this class to be defined as an inline inner class in which the user can override methods
     * @param mainActivity 
     * @param webView2 
     * @param loadingView2 
     * @param videoLayout 
     * @param nonVideoLayout 
     */
    public VideoEnabledWebChromeClient(View nonVideoLayout, ViewGroup videoLayout, View loadingView2, VideoEnabledWebView webView2, Context mainActivity)
    {
    	 this.activityNonVideoView = activityNonVideoView;
         this.activityVideoView = activityVideoView;
         this.loadingView = loadingView;
         this.webView = webView2;
         this.isVideoFullscreen = false;
         this.c=mainActivity;
    }

    /**
     * Builds a video enabled WebChromeClient.
     * @param activityNonVideoView A View in the activity's layout that contains every other view that should be hidden when the video goes full-screen.
     * @param activityVideoView A ViewGroup in the activity's layout that will display the video. Typically you would like this to fill the whole layout.
     */
    public VideoEnabledWebChromeClient(View activityNonVideoView, ViewGroup activityVideoView)
    {
        this.activityNonVideoView = activityNonVideoView;
        this.activityVideoView = activityVideoView;
        this.loadingView = null;
        this.webView = null;
        this.isVideoFullscreen = false;
    }

    /**
     * Builds a video enabled WebChromeClient.
     * @param activityNonVideoView A View in the activity's layout that contains every other view that should be hidden when the video goes full-screen.
     * @param activityVideoView A ViewGroup in the activity's layout that will display the video. Typically you would like this to fill the whole layout.
     * @param loadingView A View to be shown while the video is loading (typically only used in API level <11). Must be already inflated and without a parent view.
     * @param webView2 
     */
    public VideoEnabledWebChromeClient(View activityNonVideoView, ViewGroup activityVideoView, View loadingView, WebView webView2)
    {
        this.activityNonVideoView = activityNonVideoView;
        this.activityVideoView = activityVideoView;
        this.loadingView = loadingView;
        this.webView = (VideoEnabledWebView) webView2;
        this.isVideoFullscreen = false;
    }

    /**
     * Builds a video enabled WebChromeClient.
     * @param activityNonVideoView A View in the activity's layout that contains every other view that should be hidden when the video goes full-screen.
     * @param activityVideoView A ViewGroup in the activity's layout that will display the video. Typically you would like this to fill the whole layout.
     * @param loadingView A View to be shown while the video is loading (typically only used in API level <11). Must be already inflated and without a parent view.
     * @param webView The owner VideoEnabledWebView. Passing it will enable the VideoEnabledWebChromeClient to detect the HTML5 video ended event and exit full-screen.
     * Note: The web page must only contain one video tag in order for the HTML5 video ended event to work. This could be improved if needed (see Javascript code).
     */
    public VideoEnabledWebChromeClient(View activityNonVideoView, ViewGroup activityVideoView, View loadingView, VideoEnabledWebView webView)
    {
        this.activityNonVideoView = activityNonVideoView;
        this.activityVideoView = activityVideoView;
        this.loadingView = loadingView;
        this.webView = webView;
        this.isVideoFullscreen = false;
    }

    /**
     * Indicates if the video is being displayed using a custom view (typically full-screen)
     * @return true it the video is being displayed using a custom view (typically full-screen)
     */
    public boolean isVideoFullscreen()
    {
        return isVideoFullscreen;
    }

    /**
     * Set a callback that will be fired when the video starts or finishes displaying using a custom view (typically full-screen)
     * @param callback A VideoEnabledWebChromeClient.ToggledFullscreenCallback callback
     */
    public void setOnToggledFullscreen(ToggledFullscreenCallback callback)
    {
        this.toggledFullscreenCallback = callback;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback)
    {
        if (view instanceof FrameLayout)
        {
        	Log.d("sdf", webView.getTitle()+webView.getUrl());;
            // A video wants to be shown
        	StackParser.url=webView.getUrl();
        	c.startActivity(new Intent(c, StackParser.class));
        	webView.loadUrl(webView.getUrl());
        	/*
            FrameLayout frameLayout = (FrameLayout) view;
            View focusedChild = frameLayout.getFocusedChild();

            // Save video related variables
            this.isVideoFullscreen = true;
            this.videoViewContainer = frameLayout;
            this.videoViewCallback = callback;

            // Hide the non-video view, add the video view, and show it
            activityNonVideoView.setVisibility(View.INVISIBLE);
            activityVideoView.addView(videoViewContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            activityVideoView.setVisibility(View.VISIBLE);

            if (focusedChild instanceof VideoView)
            {
                // VideoView (typically API level <11)
                VideoView videoView = (VideoView) focusedChild;
                // Handle all the required events
                videoView.setOnPreparedListener(this);
                videoView.setOnCompletionListener(this);
                videoView.setOnErrorListener(this);
            }
            else // Usually android.webkit.HTML5VideoFullScreen$VideoSurfaceView, sometimes android.webkit.HTML5VideoFullScreen$VideoTextureView
            {
                // HTML5VideoFullScreen (typically API level 11+)
                // Handle HTML5 video ended event
                if (webView != null && webView.getSettings().getJavaScriptEnabled())
                {
                
                    // Run javascript code that detects the video end and notifies the interface
                    String js = "javascript:";
                    js += "_ytrp_html5_video = document.getElementsByTagName('video')[0];";
                    js += "if (_ytrp_html5_video !== undefined) {";
                    {
                        js += "function _ytrp_html5_video_ended() {";
                        {
                            js += "_ytrp_html5_video.removeEventListener('ended', _ytrp_html5_video_ended);";
                            js += "_VideoEnabledWebView.notifyVideoEnd();"; // Must match Javascript interface name and method of VideoEnableWebView
                        }
                        js += "}";
                        js += "_ytrp_html5_video.addEventListener('ended', _ytrp_html5_video_ended);";
                    }
                    js += "}";
                    
                //	Log.d("sdf", js);
                    webView.loadUrl(js);
                }
            }

            // Notify full-screen change
            if (toggledFullscreenCallback != null)
            {
                toggledFullscreenCallback.toggledFullscreen(true);
            }*/
        }
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) // Only available in API level 14+
    {
        onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView()
    {
        // This method must be manually (internally) called on video end in the case of VideoView (typically API level <11)
        // This method must be manually (internally) called on video end in the case of HTML5VideoFullScreen (typically API level 11+) because it's not always called automatically
        // This method must be manually (internally) called on back key press (from this class' onBackPressed() method)

        if (isVideoFullscreen)
        {
            // Hide the video view, remove it, and show the non-video view
            activityVideoView.setVisibility(View.INVISIBLE);
            activityVideoView.removeView(videoViewContainer);
            activityNonVideoView.setVisibility(View.VISIBLE);

            // Call back
            if (videoViewCallback != null) videoViewCallback.onCustomViewHidden();

            // Reset video related variables
            isVideoFullscreen = false;
            videoViewContainer = null;
            videoViewCallback = null;

            // Notify full-screen change
            if (toggledFullscreenCallback != null)
            {
                toggledFullscreenCallback.toggledFullscreen(false);
            }
        }
    }

    @Override
    public View getVideoLoadingProgressView() // Video will start loading, only called in the case of VideoView (typically API level <11)
    {
        if (loadingView != null)
        {
            loadingView.setVisibility(View.VISIBLE);
            return loadingView;
        }
        else
        {
            return super.getVideoLoadingProgressView();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) // Video will start playing, only called in the case of VideoView (typically API level <11)
    {
        if (loadingView != null)
        {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) // Video finished playing, only called in the case of VideoView (typically API level <11)
    {
        onHideCustomView();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) // Error while playing video, only called in the case of VideoView (typically API level <11)
    {
        return false; // By returning false, onCompletion() will be called
    }

    /**
     * Notifies the class that the back key has been pressed by the user.
     * This must be called from the Activity's onBackPressed(), and if it returns false, the activity itself should handle it. Otherwise don't do anything.
     * @return Returns true if the event was handled, and false if it is not (video view is not visible)
     */
    public boolean onBackPressed()
    {
        if (isVideoFullscreen)
        {
            onHideCustomView();
            return true;
        }
        else
        {
            return false;
        }
    }

}