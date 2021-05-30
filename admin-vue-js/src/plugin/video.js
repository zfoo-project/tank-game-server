import Vue from 'vue';

import Video from 'video.js';
import 'video.js/dist/video-js.css';
import zh_CN from 'video.js/dist/lang/zh-CN.json';
import en from 'video.js/dist/lang/en.json';

Vue.prototype.$video = Video;

Video.addLanguage('zh-CN', zh_CN);
Video.addLanguage('en', en);
