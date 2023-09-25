import{av as O,b0 as q,ac as C,r as y,b1 as $,_ as F,o as w,a as H,d as J,f as Y,h as G,i as b,w as s,aM as K,k as N,s as V,b as n,a8 as Q,aa as X,l as Z,q as U,e as g,t as _,aR as ee,at as te,m as k,au as ae,p as B,aY as re,j as se}from"./index.78d81d6b.js";const{Axios:ye,AxiosError:L,CanceledError:ge,isCancel:we,CancelToken:xe,VERSION:Ce,all:be,Cancel:Ve,isAxiosError:Pe,spread:Oe,toFormData:ke,AxiosHeaders:Te,formToJSON:Ie,mergeConfig:je}=O;var ne=Object.defineProperty,oe=Object.defineProperties,le=Object.getOwnPropertyDescriptors,R=Object.getOwnPropertySymbols,ie=Object.prototype.hasOwnProperty,ce=Object.prototype.propertyIsEnumerable,M=(e,a,t)=>a in e?ne(e,a,{enumerable:!0,configurable:!0,writable:!0,value:t}):e[a]=t,P=(e,a)=>{for(var t in a||(a={}))ie.call(a,t)&&M(e,t,a[t]);if(R)for(var t of R(a))ce.call(a,t)&&M(e,t,a[t]);return e},z=(e,a)=>oe(e,le(a));function de(...e){const a=typeof e[0]=="string"?e[0]:void 0,t=q(a)?1:0;let p={},m=O,f={immediate:!!t,shallow:!0};const c=r=>!!(r!=null&&r.request);e.length>0+t&&(c(e[0+t])?m=e[0+t]:p=e[0+t]),e.length>1+t&&c(e[1+t])&&(m=e[1+t]),(e.length===2+t&&!c(e[1+t])||e.length===3+t)&&(f=e[e.length-1]);const o=C(),T=f.shallow?C():y(),l=y(!1),d=y(!1),u=y(!1),v=C(),I=O.CancelToken.source(),j=r=>{l.value||!d.value||(I.cancel(r),u.value=!0,d.value=!1,l.value=!1)},E=r=>{d.value=r,l.value=!r},W=()=>new Promise((r,i)=>{$(l).toBe(!0).then(()=>r(S)).catch(i)}),x=(r,i)=>W().then(r,i),A=(r=a,i={})=>{v.value=void 0;const D=typeof r=="string"?r:a!=null?a:i.url;return D===void 0?(v.value=new L(L.ERR_INVALID_URL),l.value=!0,{then:x}):(E(!0),m(D,z(P(P({},p),typeof r=="object"?r:i),{cancelToken:I.token})).then(h=>{o.value=h,T.value=h.data}).catch(h=>{v.value=h}).finally(()=>E(!1)),{then:x})};f.immediate&&a&&A();const S={response:o,data:T,error:v,finished:l,loading:d,isFinished:l,isLoading:d,cancel:j,isAborted:u,canceled:u,aborted:u,isCanceled:u,abort:j,execute:A};return z(P({},S),{then:x})}const ue={},_e={class:"loader"};function pe(e,a){return w(),H("span",_e)}const me=F(ue,[["render",pe],["__scopeId","data-v-11a42653"],["__file","D:/Project/tank-game-server-ts/admin-vue3-ts/src/components/loading/Loading01.vue"]]),fe=k("h2",null,"Mock",-1),ve=J({__name:"Masonry",setup(e){const{data:a,isLoading:t}=de("https://picsum.photos/v2/list?page=2&limit=20"),p=Y(()=>a);return(m,f)=>{const c=G("masonry-wall");return w(),b(K,null,{default:s(()=>[N(t)?(w(),b(V,{key:0,class:"d-flex align-center justify-center",color:"primary","min-height":"1000"},{default:s(()=>[n(me)]),_:1})):(w(),b(V,{key:1},{default:s(()=>[n(c,{items:N(p),"ssr-columns":1,padding:30},{default:s(({item:o})=>[n(V,{class:"ma-3"},{default:s(()=>[n(Q,{class:"align-end text-white",src:o.download_url,"lazy-src":o.download_url,cover:""},{placeholder:s(()=>[n(X,{class:"fill-height ma-0",align:"center",justify:"center"},{default:s(()=>[n(Z,{indeterminate:"",color:"grey-lighten-5"})]),_:1})]),default:s(()=>[n(U,null,{default:s(()=>[g("Title"+_(o.author),1)]),_:2},1024)]),_:2},1032,["src","lazy-src"]),n(ee,{class:"pt-4"},{default:s(()=>[g(" The "+_(o.id)+" item ",1)]),_:2},1024),n(te,null,{default:s(()=>[k("div",null,"height:"+_(o.height)+" width:"+_(o.width),1),k("div",null,_(o.download_url),1)]),_:2},1024),n(ae,null,{default:s(()=>[n(B,{color:"primary"},{default:s(()=>[g(" Share ")]),_:1}),n(B,{color:"primary"},{default:s(()=>[g(" Explore ")]),_:1})]),_:1})]),_:2},1024)]),_:1},8,["items"])]),_:1})),n(re,{class:"my-5"}),fe,se(` <masonry-wall\r
      :items="items"\r
      :ssr-columns="1"\r
      :column-width="300"\r
      :padding="30"\r
    >\r
      <template #default="{ item, index }">\r
        <v-card class="ma-3" max-width="400">\r
          <v-img\r
            class="align-end text-white"\r
            :src="\r
              index % 2 === 0\r
                ? 'https://picsum.photos/200/300?random=' + index\r
                : 'https://picsum.photos/200?random=' + index\r
            "\r
            :lazy-src="\r
              index % 2 === 0\r
                ? 'https://picsum.photos/200/300?random=' + index\r
                : 'https://picsum.photos/200?random=' + index\r
            "\r
            cover\r
          >\r
            <v-card-title>Title{{ item.color }}</v-card-title>\r
          </v-img>\r
\r
          <v-card-subtitle class="pt-4"> The {{ index }} item </v-card-subtitle>\r
\r
          <v-card-text>\r
            <div>Whitehaven Beach</div>\r
\r
            <div>Whitsunday Island, Whitsunday Islands</div>\r
          </v-card-text>\r
\r
          <v-card-actions>\r
            <v-btn :color="item.color"> Share </v-btn>\r
\r
            <v-btn :color="item.color"> Explore </v-btn>\r
          </v-card-actions>\r
        </v-card>\r
      </template>\r
    </masonry-wall> `)]),_:1})}}}),Ee=F(ve,[["__file","D:/Project/tank-game-server-ts/admin-vue3-ts/src/views/ui/Masonry.vue"]]);export{Ee as default};
