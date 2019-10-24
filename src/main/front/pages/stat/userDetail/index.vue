<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header pb-0">
                        <div class="form-row float-right align-items-center">
                            <div class="col-auto">
                                <b-form-select
                                        :required="true"
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
                                        >-- 요금제 --</option>
                                    </template>
                                </b-form-select>
                            </div>
                            <div class="col-auto">
                                <b-form-select
                                        :required="true"
                                        v-model="params.addItemCode"
                                        :options="addInfoItems"
                                        :value-field="'addItemCode'"
                                        :text-field="'addItemNm'"
                                        :size="'sm'"
                                >
                                    <template v-slot:first>
                                        <option
                                                :value="''"
                                        >-- 상품 --</option>
                                    </template>
                                </b-form-select>
                            </div>
                            <div class="col-auto">
                                <label>가입기간</label>
                            </div>
                            <div class="col-auto">
                                <b-form-input
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
                                        v-if="searchType === 'SUBNO'"
                                        v-model="params.subNo"
                                        type="number"
                                        :size="'sm'"
                                        @keydown.enter="selectUserDetailStat"
                                ></b-form-input>
                                <b-form-input
                                        v-if="searchType === 'CTN'"
                                        v-model="params.ctn"
                                        :size="'sm'"
                                        @keydown.enter="selectUserDetailStat"
                                ></b-form-input>
                            </div>
                            <div class="col-auto">
                                <b-button @click="selectUserDetailStat" class="btn-primary">검색</b-button>
                            </div>
                            <div class="col-auto">
                                <download-excel
                                        :class="'btn btn-primary btn-secondary'"
                                        :name    = "'가입자상세리스트.xls'"
                                        :fields="excelFields"
                                        :data='items'>
                                    엑셀
                                </download-excel>
                            </div>
                        </div>
                    </div>
                    <div class="card-content collapse show">
                        <div class="card-body">
                            <div class="table-responsive">
                                <b-table
                                        :items="items"
                                        :fields="fields"
                                        :bordered="true"
                                >
                                    <b-thead>

                                    </b-thead>
                                </b-table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
    import JsonExcel from 'vue-json-excel';
    export default {
        name: "index",

        components: {
            downloadExcel : JsonExcel
        },

        beforeMount() {
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
                    {key: 'createDt', label: '가입일시'},
                    {key: 'lastLoginDt', label: '마지막로그인'},
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
                searchType: 'CTN',
                entrItems: [],
                addInfoItems : [],
                excelFields:{
                    'GFN ID': 'gfnId',
                    'CTN': 'ctn',
                    '가입자번호': 'subNo',
                    '상품구분': 'prodCd',
                    '요금제코드': 'entrItemCode',
                    '요금제명': 'entrItemNm',
                    '부가서비스코드': 'addItemCode',
                    '부가서비스명': 'addItemNm',
                    '상태': 'ctnStusCode',
                    '가입일시': 'createDt',
                    '마지막로그인': 'lastLoginDt',
                }
            };
        },

        computed:{
            date(){
                return this.$moment().format('YYYYMMDD');
            }
        },

        methods:{
            async selectUserDetailStat() {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/stat/selectUserDetailStat', this.params);
                    this.items = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async selectEntrItemList() {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/entr/selectEntrItemList', { statusCd : this.searchUseYn});
                    this.entrItems = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async getAddItemList(entrItemCode) {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/add/selectAddItemList', { entrItemCode});
                    this.addInfoItems = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            changeSearchType() {
                this.$set(this.params, 'subNo', '');
                this.$set(this.params, 'ctn', '');
            },
        },
    }
</script>

<style scoped>

</style>