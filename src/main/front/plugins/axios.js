import Cookie from 'js-cookie';

export default function ({ $axios, redirect, store, $bvModal }) {
    $axios.onRequest(async request => {
        setCsrf(request);

        return request;
    })

    $axios.onResponse(async response => {
        return response;
    });

    // $axios.onError(error => {
    //     store.commit('setError', error.response.data);
    //
    //     return error;
    // });

    function saveCsrfFromResponse(response) {
        const cookies = response.headers['set-cookie'];
        if(!cookies) return;

        const csrf = getCsrfTokenFrom(cookies);
        console.log(`csrf : ${csrf}`);
        if(csrf) setCsrfToCookie(csrf);
    }

    function getCsrfTokenFrom(cookies) {
        const strCookie = cookies.find(c => c.includes('XSRF-TOKEN'));
        const m = /XSRF-TOKEN=(.+);/.exec(strCookie);
        if(!m) return;

        return m[1];
    }

    function setCsrfToCookie(csrf) {
        store.commit('setCsrf', csrf);
    }

    function setCsrf(request) {
        try {
            const csrf = Cookie.get('XSRF-TOKEN');
            if(csrf) request.headers['X-XSRF-TOKEN'] = csrf;

        } catch (e) {
            console.log(e);
        }
    }

    function setToken(request) {
        $axios.setToken(store.state.login.user.access_token, 'bearer');
    }
}

