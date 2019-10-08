import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export default new Router({
  mode: "hash",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "home",
      props: {
        apiUrl: process.env.VUE_APP_API_URL,
        tracklist: [
          {
            levelFilter: (level: number) => level >= 150,
            file: "https://freesound.org/data/previews/117/117616_1160789-lq.mp3",
          },
          {
            levelFilter: (level: number) => level >= 130 && level <= 149,
            file: "https://freesound.org/data/previews/131/131930_1542102-lq.mp3",
          },
          {
            levelFilter: (level: number) => level >= 110 && level <= 129,
            file: "https://freesound.org/data/previews/461/461236_9676720-lq.mp3",
          },
          {
            levelFilter: (level: number) => level >= 100 && level <= 109,
            file: "https://freesound.org/data/previews/207/207457_3841995-lq.mp3",
          },
          {
            levelFilter: (level: number) => level >= 90 && level <= 99,
            file: "https://freesound.org/data/previews/83/83486_791247-lq.mp3",
          },
          {
            levelFilter: (level: number) => level >= 71 && level <= 89,
            file: "https://freesound.org/data/previews/262/262889_4930786-lq.mp3",
          },
          {
            levelFilter: (level: number) => level >= 50 && level <= 70,
            file: "https://freesound.org/data/previews/457/457895_2741562-lq.mp3",
          },
          {
            levelFilter: (level: number) => level >= 20 && level <= 49,
            file: "https://freesound.org/data/previews/172/172941_1999691-lq.mp3",
          },
          {
            levelFilter: (level: number) => level >= 0 && level <= 19,
            file: "https://freesound.org/data/previews/112/112556_591423-lq.mp3",
          },
        ],
      },
      component: () => import("@/views/Home.vue"),
    },
  ],
});
