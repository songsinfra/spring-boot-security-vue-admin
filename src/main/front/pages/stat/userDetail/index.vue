<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header pb-0">
                        <b-form @submit.prevent="selectUserDetailStat">
                            <div class="form-row float-right align-items-center">
                                <div class="col-auto">
                                    <b-form-select
                                            v-model="params.entrItemCode"
                                            :options="entrItems"
                                            :value-field="'entrItemCode'"
                                            :text-field="'entrItemNm'"
                                            :size="'sm'"
                                            placeholder="요금제"
                                    >
                                        <template v-slot:first>
                                            <option
                                                    :value="''"
                                            >-- 요금제 --
                                            </option>
                                        </template>
                                    </b-form-select>
                                </div>
                                <div class="col-auto">
                                    <b-form-select
                                            v-model="params.addItemCode"
                                            :options="addInfoItems"
                                            :value-field="'addItemCode'"
                                            :text-field="'addItemNm'"
                                            :size="'sm'"
                                    >
                                        <template v-slot:first>
                                            <option
                                                    :value="''"
                                            >-- 상품 --
                                            </option>
                                        </template>
                                    </b-form-select>
                                </div>
                                <div class="col-auto">
                                    <label>가입기간</label>
                                </div>
                                <div class="col-auto">
                                    <b-form-input
                                            :required="true"
                                            v-model="params.createStartDt"
                                            type="date"
                                            :size="'sm'"
                                            placeholder="가입시작기간"
                                    ></b-form-input>
                                </div>
                                <div class="col-auto">
                                    ~
                                </div>
                                <div class="col-auto">
                                    <b-form-input
                                            :required="true"
                                            v-model="params.createEndDt"
                                            type="date"
                                            :size="'sm'"
                                            placeholder="가입종료기간"
                                    ></b-form-input>
                                </div>
                                <div class="col-auto">
                                    <b-form-select
                                            v-model="searchType"
                                            @change="changeSearchType"
                                            :size="'sm'"
                                    >
                                        <option value="SUBNO">가입자번호</option>
                                        <option value="CTN">CTN</option>
                                    </b-form-select>
                                </div>
                                <div class="col-auto">
                                    <b-form-input
                                            v-show="searchType === 'SUBNO'"
                                            v-model="params.subNo"
                                            :size="'sm'"
                                            @keydown.enter="selectUserDetailStat"
                                            v-validate="'numeric|max:12'"
                                            data-vv-name="가입자번호"
                                    ></b-form-input>
                                    <b-form-input
                                            v-show="searchType === 'CTN'"
                                            v-model="params.ctn"
                                            :size="'sm'"
                                            @keydown.enter="selectUserDetailStat"
                                            v-validate="'numeric|max:20'"
                                            :state="params.ctn.$dirty ? !params.ctn.name.$error : null"
                                            data-vv-name="연락처"
                                    ></b-form-input>
                                </div>
                                <div class="col-auto">
                                    <b-button type="submit" class="btn-primary">검색</b-button>
                                </div>
                                <div class="col-auto">
                                    <download-excel
                                            :class="'btn btn-primary btn-secondary'"
                                            :name="'가입자상세리스트.xls'"
                                            :fields="excelFields"
                                            :data='items'>
                                        엑셀
                                    </download-excel>
                                </div>
                            </div>
                        </b-form>
                    </div>
                    <div class="card-content collapse show">
                        <div class="card-body">
                            <div class="table-responsive">
                                <b-table
                                        :items="items"
                                        :fields="fields"
                                        :bordered="true"
                                        :per-page="perPage"
                                        :current-page="currentPage"
                                >
                                    <template v-slot:optionTosList="{item}">
                                        <b-button @click="popupTosDetail(item.gfnId)">{{item.optionTosList || '상세'}}</b-button>
                                    </template>
                                </b-table>
                                <b-pagination
                                        v-model="currentPage"
                                        :total-rows="tableRows"
                                        :per-page="perPage"
                                        aria-controls="my-table"
                                        :align="'center'"
                                ></b-pagination>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <ModalTosDetail :gfn-id="selectedGfnId"></ModalTosDetail>
    </section>
</template>

<script>
    import JsonExcel from 'vue-json-excel';
    import ModalTosDetail from "~/components/pages/stat/userDetail/ModalTosDetail";

    export default {
        name: "index",

        components: {
            downloadExcel : JsonExcel,
            ModalTosDetail
        },

        computed: {
            tableRows() {
                return this.items.length
            },
            yesterday(){
                return this.$moment().subtract(1, 'days').format('YYYY-MM-DD');
            }
        },
        async created() {
            this.$eventBus.$on('refreshUserDetail', this.selectUserDetailStat);
        },

        beforeMount() {
            this.$set(this.params, "createStartDt", this.yesterday);
            this.$set(this.params, "createEndDt", this.yesterday);

            this.selectUserDetailStat();
            this.selectEntrItemList();
            this.getAddItemList();
        },

        data() {
            return {
                fields: [
                    {key: 'gfnId', label: 'GFN ID'},
                    {key: 'ctn', label: 'CTN'},
                    {key: 'subNo', label: '가입자번호'},
                    {key: 'prodCd', label: '상품구분'},
                    {key: 'entrItemCode', label: '요금제코드'},
                    {key: 'entrItemNm', label: '요금제명'},
                    {key: 'addItemCode', label: '부가서비스코드'},
                    {key: 'addItemNm', label: '부가서비스명'},
                    {key: 'ctnStusCode', label: '상태'},
                    {key: 'createDt', label: 'GFN 생성일시'},
                    {key: 'lastLoginDt', label: '마지막로그인'},
                    {key: 'subscribeDt', label: '부가서비스 가입일시'},
                    {key: 'unsubscribeDt', label: '부가서비스 해지일시'},
                    {key: 'grantDt', label: 'NVIDIA 등록일시'},
                    {key: 'optionTosList', label: '선택약관동의'},
                ],
                items:[],
                params: {
                    entrItemCode: '',
                    addItemCode: '',
                    createStartDt: '',
                    createEndDt: '',
                    subNo: '',
                    ctn: '',
                },
                selectedGfnId: '',
                searchType: 'CTN',
                entrItems: [],
                addInfoItems : [],
                excelFields:{
                    'GFN ID': 'gfnId',
                    'CTN': {
                        field: 'ctn',
                        callback: this.convertToStringForExcel
                    },
                    'SUB_NO': {
                        field: 'subNo',
                        callback: this.convertToStringForExcel
                    },
                    'PROD_CD': {
                        field: 'prodCd',
                        callback: this.convertToStringForExcel
                    },
                    'ENTR_ITEM_CODE': {
                        field: 'entrItemCode',
                        callback: this.convertToStringForExcel
                    },
                    'ENTR_ITEM_NM': 'entrItemNm',
                    'ADD_ITEM_CODE': {
                        field: 'addItemCode',
                        callback: this.convertToStringForExcel
                    },
                    'ADD_ITEM_NM': 'addItemNm',
                    'CTN_STUS_CODE': {
                        field: 'ctnStusCode',
                        callback: this.convertToStringForExcel
                    },
                    'CREATE_DT': {
                        field: 'createDt',
                        callback: this.convertToStringForExcel
                    },
                    'LAST_LOGIN_DT': {
                        field: 'lastLoginDt',
                        callback: this.convertToStringForExcel
                    },
                    'SUBSCRIBE_DT' : {
                        field : 'subscribeDt',
                        callback: this.convertToStringForExcel
                    },
                    'UNSUBSCRIBE_DT' : {
                        field : 'unsubscribeDt',
                        callback: this.convertToStringForExcel
                    },
                    'GRANT_DT' : {
                        field : 'grantDt',
                        callback: this.convertToStringForExcel
                    },
                    'OPTION_TOS_LIST' : {
                        field : 'optionTosList',
                        callback: this.convertToStringForExcel
                    },
                },
                perPage: 10,
                currentPage: 1,
            };
        },

        methods:{
            async selectUserDetailStat() {
                try {
                    this.validateField();

                    const params = {...this.params};
                    this.checkAndConvertDate(params);

                    const {data} = await this.$axios.post(process.env.contextPath + '/stat/selectUserDetailStat', params);
                    this.items = data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async selectEntrItemList() {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/entr/selectEntrItemList', { statusCd : this.searchUseYn});
                    this.entrItems = data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async getAddItemList(entrItemCode) {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/add/selectAddItemList', { entrItemCode});
                    this.addInfoItems = data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            changeSearchType() {
                this.$set(this.params, 'subNo', '');
                this.$set(this.params, 'ctn', '');
            },

            convertToStringForExcel(value) {
                return '=\"' + value + '\"';
            },

            popupTosDetail(gfnId) {
                this.$nextTick(async () => {
                    this.selectedGfnId = gfnId;
                    this.$bvModal.show('modal_Tos_detail');
                });
            },

            checkAndConvertDate(params){
                if(!params.createStartDt) throw Error("시작날짜를 입력해주세요");
                if(!params.createEndDt) throw Error("종료날짜를 입력해주세요");

                params.createStartDt = params.createStartDt.replace(/-/g, '');
                params.createEndDt = params.createEndDt.replace(/-/g, '');

                const start = this.$moment(params.createStartDt, 'YYYYMMDD');
                const end = this.$moment(params.createEndDt, 'YYYYMMDD');

                if(start.diff(end, 'days') > 0) throw Error("시작날짜가 종료날짜보다 빠릅니다.");
                if(start.diff(end, 'months') < 0 ) {
                    this.$set(this.params, 'createEndDt', start.add(1, 'months').subtract(1, 'days').format("YYYY-MM-DD"));
                    throw Error("검색기간은 1개월 이하만 입력해주세요");
                }
            },

            validateField(){
                this.$validator.validateAll()
                if (this.errors.any()) {
                    throw Error(this.errors.items[0].msg);
                }
            }
        },
    }
</script>

<style scoped>

</style>
