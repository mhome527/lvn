package teach.vietnam.asia.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 3/19/15.
 */
public class InfoAppEntity {
    @SerializedName("update")
    public long updated;

    @SerializedName("app")
    public String app_id;

    @SerializedName("published")
    public String published;

    @SerializedName("version")
    public String version_name;
}
