package com.myideaway.easyapp.core.lib.bean;

import java.util.List;

/**
 * Created by deng on 16-2-22.
 */
public class Weather {

    /**
     * error : 0
     * status : success
     * date : 2016-02-22
     * results : [{"currentCity":"北京","pm25":"182","index":[{"title":"穿衣","zs":"较冷","tipt":"穿衣指数","des":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"},{"title":"洗车","zs":"较适宜","tipt":"洗车指数","des":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},{"title":"旅游","zs":"一般","tipt":"旅游指数","des":"天气较好，温度稍低，加之风稍大，让人感觉有点凉，会对外出有一定影响，外出注意防风保暖。"},{"title":"感冒","zs":"易发","tipt":"感冒指数","des":"昼夜温差大，且空气湿度较大，易发生感冒，请注意适当增减衣服，加强自我防护避免感冒。"},{"title":"运动","zs":"较不宜","tipt":"运动指数","des":"天气较好，但考虑天气寒冷，风力较强，推荐您进行室内运动，若在户外运动请注意保暖并做好准备活动。"},{"title":"紫外线强度","zs":"中等","tipt":"紫外线强度指数","des":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}],"weather_data":[{"date":"周一 02月22日 (实时：4℃)","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/qing.png","weather":"晴","wind":"北风3-4级","temperature":"8 ~ -4℃"},{"date":"周二","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/qing.png","weather":"晴","wind":"北风3-4级","temperature":"4 ~ -6℃"},{"date":"周三","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/duoyun.png","weather":"晴转多云","wind":"微风","temperature":"7 ~ -4℃"},{"date":"周四","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/duoyun.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/qing.png","weather":"多云转晴","wind":"微风","temperature":"7 ~ -3℃"}]}]
     */

    private int error;
    private String status;
    private String date;
    /**
     * currentCity : 北京
     * pm25 : 182
     * index : [{"title":"穿衣","zs":"较冷","tipt":"穿衣指数","des":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"},{"title":"洗车","zs":"较适宜","tipt":"洗车指数","des":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},{"title":"旅游","zs":"一般","tipt":"旅游指数","des":"天气较好，温度稍低，加之风稍大，让人感觉有点凉，会对外出有一定影响，外出注意防风保暖。"},{"title":"感冒","zs":"易发","tipt":"感冒指数","des":"昼夜温差大，且空气湿度较大，易发生感冒，请注意适当增减衣服，加强自我防护避免感冒。"},{"title":"运动","zs":"较不宜","tipt":"运动指数","des":"天气较好，但考虑天气寒冷，风力较强，推荐您进行室内运动，若在户外运动请注意保暖并做好准备活动。"},{"title":"紫外线强度","zs":"中等","tipt":"紫外线强度指数","des":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}]
     * weather_data : [{"date":"周一 02月22日 (实时：4℃)","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/qing.png","weather":"晴","wind":"北风3-4级","temperature":"8 ~ -4℃"},{"date":"周二","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/qing.png","weather":"晴","wind":"北风3-4级","temperature":"4 ~ -6℃"},{"date":"周三","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/duoyun.png","weather":"晴转多云","wind":"微风","temperature":"7 ~ -4℃"},{"date":"周四","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/duoyun.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/qing.png","weather":"多云转晴","wind":"微风","temperature":"7 ~ -3℃"}]
     */

    private List<ResultsEntity> results;

    public void setError(int error) {
        this.error = error;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public int getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public static class ResultsEntity {
        private String currentCity;
        private String pm25;
        /**
         * title : 穿衣
         * zs : 较冷
         * tipt : 穿衣指数
         * des : 建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。
         */

        private List<IndexEntity> index;
        /**
         * date : 周一 02月22日 (实时：4℃)
         * dayPictureUrl : http://api.map.baidu.com/images/weather/day/qing.png
         * nightPictureUrl : http://api.map.baidu.com/images/weather/night/qing.png
         * weather : 晴
         * wind : 北风3-4级
         * temperature : 8 ~ -4℃
         */

        private List<WeatherDataEntity> weather_data;

        public void setCurrentCity(String currentCity) {
            this.currentCity = currentCity;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public void setIndex(List<IndexEntity> index) {
            this.index = index;
        }

        public void setWeather_data(List<WeatherDataEntity> weather_data) {
            this.weather_data = weather_data;
        }

        public String getCurrentCity() {
            return currentCity;
        }

        public String getPm25() {
            return pm25;
        }

        public List<IndexEntity> getIndex() {
            return index;
        }

        public List<WeatherDataEntity> getWeather_data() {
            return weather_data;
        }

        public static class IndexEntity {
            private String title;
            private String zs;
            private String tipt;
            private String des;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setZs(String zs) {
                this.zs = zs;
            }

            public void setTipt(String tipt) {
                this.tipt = tipt;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getTitle() {
                return title;
            }

            public String getZs() {
                return zs;
            }

            public String getTipt() {
                return tipt;
            }

            public String getDes() {
                return des;
            }
        }

        public static class WeatherDataEntity {
            private String date;
            private String dayPictureUrl;
            private String nightPictureUrl;
            private String weather;
            private String wind;
            private String temperature;

            public void setDate(String date) {
                this.date = date;
            }

            public void setDayPictureUrl(String dayPictureUrl) {
                this.dayPictureUrl = dayPictureUrl;
            }

            public void setNightPictureUrl(String nightPictureUrl) {
                this.nightPictureUrl = nightPictureUrl;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getDate() {
                return date;
            }

            public String getDayPictureUrl() {
                return dayPictureUrl;
            }

            public String getNightPictureUrl() {
                return nightPictureUrl;
            }

            public String getWeather() {
                return weather;
            }

            public String getWind() {
                return wind;
            }

            public String getTemperature() {
                return temperature;
            }
        }
    }

    @Override
    public String toString() {
        return "Wearher{" +
                "error=" + error +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", results=" + results +
                '}';
    }
}
