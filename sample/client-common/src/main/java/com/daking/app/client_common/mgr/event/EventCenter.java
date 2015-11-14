package com.daking.app.client_common.mgr.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 事件中心(请使用EventBus开源库)
 * Created by daking on 15/8/13.
 */
public class EventCenter {
    private volatile static EventCenter eventCenter;

    private Map<String,Vector<EventCenter.CallBack>> listeners; // 事件回调字典,键为事件类型,值为回调队列

    /**
     * 事件中心回调
     */
    public interface CallBack
    {
        public void eventHandler( ClientEvent event );
    }

    public EventCenter()
    {
        listeners = new HashMap<String,Vector<EventCenter.CallBack>>();
    }

    public static EventCenter getInstance()
    {
        if( eventCenter==null ) {
            synchronized ( EventCenter.class ) {
                if ( eventCenter==null ) {
                    eventCenter = new EventCenter();
                }
            }
        }
        return eventCenter;
    }

    /**
     * 添加监听
     * @param type 事件类型
     * @param callBack 回调
     * @return 是否添加成功
     */
    public boolean addListener( int type, EventCenter.CallBack callBack )
    {
        if ( !hasListener( type, callBack ) )
        {
            Vector<EventCenter.CallBack> vector = listeners.get( String.valueOf(type) );
            if ( vector==null )
            {
                vector = new Vector<EventCenter.CallBack>();
                listeners.put( String.valueOf(type),vector );
            }
            vector.add( callBack );
            return true;
        }
        return false;
    }

    /**
     * 删除监听
     * @param type 事件类型
     * @param callBack 回调
     * @return 是否删除成功
     */
    public boolean removeListener( int type, EventCenter.CallBack callBack )
    {
        if ( hasListener( type, callBack ) )
        {
            String typeStr = String.valueOf( type );
            Vector<EventCenter.CallBack> vector = listeners.get( typeStr );
            vector.remove( callBack );
            if ( vector.size()==0 )
            {
                listeners.remove( typeStr );
            }
            return true;
        }
        return false;
    }

    /**
     * 删除指定事件的所有监听
     * @param type 事件类型
     * @return 是否删除成功
     */
    public boolean removeListener( int type )
    {
        if( hasListener( type ) )
        {
            listeners.remove( String.valueOf(type) );
        }
        return false;
    }

    /**
     * 删除所有监听
     */
    public void removeAllListeners()
    {
        listeners = new HashMap<String,Vector<EventCenter.CallBack>>();
    }

    /**
     * 派发自定义事件
     * @param event 自定义事件
     */
    public void dispatchEvent( ClientEvent event )
    {
        int type = event.getType();
        if ( hasListener( type ) )
        {
            Vector<EventCenter.CallBack> vector = listeners.get( String.valueOf(type) );
            for ( EventCenter.CallBack callBack : vector ) {
                if ( callBack!=null )
                {
                    callBack.eventHandler( event );
                }
            }
        }
    }

    /**
     * 是否存在指定事件类型的监听
     * @param type 事件类型
     * @return 是否存在
     */
    public boolean hasListener( int type )
    {
        Vector<EventCenter.CallBack> vector = listeners.get( String.valueOf(type) );
        return vector!=null && vector.size()>0;
    }

    /**
     * 是否存在指定事件类型以及回调的监听
     * @param type 事件类型
     * @param callBack 回调
     * @return 是否存在
     */
    public boolean hasListener( int type, EventCenter.CallBack callBack )
    {
        Vector<EventCenter.CallBack> vector = listeners.get( String.valueOf(type) );
        if ( vector!=null && vector.size()>0 )
        {
            for ( EventCenter.CallBack cb : vector ) {
                if( cb!=null && cb==callBack )
                {
                    return true;
                }
            }
        }
        return false;
    }

}
