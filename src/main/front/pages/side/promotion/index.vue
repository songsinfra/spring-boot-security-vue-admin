<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header pb-0">
                        <div class="form-row float-right align-items-center">
                            <div class="col-auto">
                                <b-form-input
                                        v-model="params.promoCode"
                                        :size="'sm'"
                                        placeholder="코드"
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
                                <b-button @click="deletePromoList" variant="danger">만료처리</b-button>
                            </div>
                            <div class="col-auto">
                                <b-button @click="createPromo" class="btn-primary">생성</b-button>
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

    export default {
        name: "index",
        components: {ModalPromotion},

        beforeMount() {
            this.selectPromoList();
        },

        async created() {
            this.$eventBus.$on('refreshPromoList', this.selectPromoList);
        },

        data() {
            return {
                fields: [
                    {key: 'select', label: '선택'},
                    {key: 'promoCode', label: '코드'},
                    {key: 'email', label: '이메일'},
                    {key: 'contactNo', label: '연락처'},
                    {key: 'createDt', label: '발급일',
                        formatter: (value) => this.convertDateFormater(value)
                    },
                    {key: 'dueDt', label: '만료일',
                        formatter: (value) => this.convertDateFormater(value)
                    },
                    {key: 'statusCd', label: '상태',
                        formatter: (value) => this.getStatusNm(value)
                    },
                    {key: 'action', label: '수정'},
                ],
                items:[],
                params: {
                    promoCode: '',
                    email: '',
                },
                selectedPromo:{},
                modalState:''
            };
        },

        methods:{
            async selectPromoList() {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/side/selectPromoList', this.params);
                    this.items = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            getStatusNm(code) {
                switch (code) {
                    case '0':
                        return '미사용';
                    case '1':
                        return '사용';
                    case '2':
                        return '만료';
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

            async deletePromoList() {
                try {
                    const deletePromoCodeList = this.items.filter(item => item.checked === 'Y')
                        .map(item=>item.promoCode);

                    if(deletePromoCodeList.length === 0){
                        await this.$bvModal.msgBoxOk('선택된 프로모션정보가 없습니다');
                        return;
                    }

                    const isOk = await this.$bvModal.msgBoxConfirm("선택된 프로모션정보를 만료처리 하시겠습니까?");
                    if (!isOk) return;

                    const {message} = await this.$axios.post(process.env.contextPath + '/side/deletePromoCode',{
                        promoCodeList : deletePromoCodeList
                    });

                    debugger;
                    await this.$bvModal.msgBoxOk(message);

                    await this.selectPromoList();
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }

            }
        },
    }
</script>

<style scoped>

</style>