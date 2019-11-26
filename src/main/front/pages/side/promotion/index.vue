<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header pb-0">
                        <div class="form-row float-right align-items-center">
                            <div class="col-auto">
                                <b-form-input
                                        v-model="params.name"
                                        :size="'sm'"
                                        placeholder="성명"
                                        @keydown.enter="selectPromoList"
                                ></b-form-input>
                            </div>
                            <div class="col-auto">
                                <b-form-input
                                        v-model="params.promoCode"
                                        :size="'sm'"
                                        placeholder="코드"
                                        maxlength="16"
                                        @keydown.enter="selectPromoList"
                                ></b-form-input>
                            </div>
                            <div class="col-auto">
                                <b-form-input
                                        v-model="params.email"
                                        type="text"
                                        placeholder="이메일"
                                        :size="'sm'"
                                        @keydown.enter="selectPromoList"
                                ></b-form-input>
                            </div>
                            <div class="col-auto">
                                <b-button @click="selectPromoList" class="btn-primary">검색</b-button>
                            </div>
                            <div class="col-auto">
                                <b-button @click="expirePromoList" variant="danger">만료</b-button>
                            </div>
                            <div class="col-auto">
                                <b-button @click="deletePromoList" variant="danger">삭제</b-button>
                            </div>
                            <div class="col-auto">
                                <b-button @click="createPromo" class="btn-primary">생성</b-button>
                            </div>
                            <div class="col-auto">
                                <download-excel
                                        :class="'btn btn-primary btn-secondary'"
                                        :name    = "'프로모션코드리스트.xls'"
                                        :fields="excelFields"
                                        :fetch='getSortedData'>
                                    엑셀
                                </download-excel>
                            </div>
                        </div>
                    </div>
                    <div class="card-content collapse show">
                        <div class="card-body">
                            <div class="table-responsive">
                                <b-table
                                        ref="grid"
                                        :items="items"
                                        :fields="fields"
                                        :bordered="true"
                                        :per-page="perPage"
                                        :current-page="currentPage"
                                >
                                    <template v-slot:select="{item}">
                                        <b-form-checkbox
                                                v-model="item.checked"
                                                name="checkbox-1"
                                                value="Y"
                                                unchecked-value="N"
                                        >
                                        </b-form-checkbox>
                                    </template>
                                    <template v-slot:action="{item}">
                                        <b-button
                                                variant="danger"
                                                @click="updatePromo(item)"
                                        >수정</b-button>
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
        <modal-promotion :selected-promo="selectedPromo" :state="modalState"/>
    </section>
</template>

<script>
    import ModalPromotion from "~/components/pages/side/promotion/ModalPromotion";
    import JsonExcel from 'vue-json-excel';

    export default {
        name: "index",

        components: {
            ModalPromotion,
            downloadExcel : JsonExcel
        },
        beforeMount() {
            this.selectPromoList();
        },

        async created() {
            this.$eventBus.$on('refreshPromoList', this.selectPromoList);
        },

        computed: {
            tableRows() {
                return this.items.length;
            },
        },

        data() {
            return {
                fields: [
                    {key: 'select', label: '선택'},
                    {key: 'promoCode', label: '코드'},
                    {key: 'name', label: '성명'},
                    {key: 'email', label: '이메일'},
                    {key: 'contactNo', label: '연락처'},
                    {key: 'createDt', label: '발급일', sortable: true,
                        formatter: (value) => this.convertDateFormater(value)
                    },
                    {key: 'dueDt', label: '만료일', sortable: true,
                        formatter: (value) => this.convertDateFormater(value)
                    },
                    {key: 'statusCd', label: '상태', sortable: true,
                        formatter: (value) => this.getStatusNm(value)
                    },
                    {key: 'action', label: '수정'},
                ],
                items:[],
                params: {
                    promoCode: '',
                    email: '',
                    name: '',
                },
                selectedPromo:{},
                modalState:'',
                perPage: 10,
                currentPage: 1,
                excelFields:{
                    '코드': 'promoCode',
                    '성명': 'name',
                    'EMAIL': 'email',
                    '연락처': {
                        field: 'contactNo',
                        callback: this.convertToStringForExcel
                    },
                    '발급일': {
                        field: 'createDt',
                        callback: this.convertDateFormater
                    },
                    '만료일': {
                        field: 'dueDt',
                        callback: this.convertDateFormater
                    },
                    '상태': {
                        field: 'statusCd',
                        callback: this.getStatusNm
                    },
                },
            };
        },

        methods:{
            async selectPromoList() {
                try {
                    const params = {...this.params};
                    if(params.promoCode) params.promoCode = params.promoCode.toUpperCase().replace(/-/g, '');

                    const {data} = await this.$axios.post(process.env.contextPath + '/side/selectPromoList', params);
                    this.items = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.response.data.message);
                }
            },

            getStatusNm(code) {
                switch (code) {
                    case '0':
                        return '미사용';
                    case '1':
                        return '사용';
                    case '2':
                        return '만료처리';
                    case '3':
                        return '기간만료';
                }
            },

            updatePromo(item) {
                this.selectedPromo = item;
                this.modalState = 'UPDATE';

                this.$nextTick(async () => {
                    this.$bvModal.show('modal_promotion');
                });
            },

            createPromo() {
                this.selectedPromo = {};
                this.modalState = 'CREATE';

                this.$nextTick(async () => {
                    this.$bvModal.show('modal_promotion');
                });
            },

            convertDateFormater(date) {
                return this.$moment(date, 'YYYYMMDD').format("YYYY.MM.DD");
            },

            async expirePromoList() {
                try {
                    const expirePromoCodeList = this.items.filter(item => item.checked === 'Y')
                        .map(item=>item.promoCode);

                    if(expirePromoCodeList.length === 0){
                        await this.$bvModal.msgBoxOk('선택된 프로모션정보가 없습니다');
                        return;
                    }

                    const isOk = await this.$bvModal.msgBoxConfirm("선택된 프로모션정보를 만료처리 하시겠습니까?");
                    if (!isOk) return;

                    const {message} = await this.$axios.post(process.env.contextPath + '/side/expirePromoCode',{
                        promoCodeList : expirePromoCodeList
                    });

                    await this.$bvModal.msgBoxOk(message);

                    await this.selectPromoList();
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }

            },

            async deletePromoList() {
                try {
                    const deletePromoCodeList = this.items.filter(item => item.checked === 'Y')
                        .map(item=>item.promoCode);

                    if(deletePromoCodeList.length === 0){
                        await this.$bvModal.msgBoxOk('선택된 프로모션정보가 없습니다');
                        return;
                    }

                    const isOk = await this.$bvModal.msgBoxConfirm("선택된 프로모션정보를 삭제처리 하시겠습니까?");
                    if (!isOk) return;

                    const {message} = await this.$axios.post(process.env.contextPath + '/side/deletePromoCode',{
                        promoCodeList : deletePromoCodeList
                    });

                    await this.$bvModal.msgBoxOk(message);

                    await this.selectPromoList();
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }

            },

            convertToStringForExcel(value) {
                return '=\"' + value + '\"';
            },

            getSortedData() {
                return this.$refs.grid.sortedItems;
            }
        },
    }
</script>

<style scoped>

</sty