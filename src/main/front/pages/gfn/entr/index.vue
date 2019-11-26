<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="card">
                            <div class="card-header pb-0">
                                <h3>등록된 가입가능서비스</h3>
                                <ul class="list-inline float-right m-0">
                                    <li class="mr-1 custom-control custom-checkbox"><b-form-checkbox
                                            v-model="searchUseYn"
                                            value="1"
                                            unchecked-value=""
                                            @change="changeCheckbox"
                                    >미사용서비스표시</b-form-checkbox>
                                    </li>
                                    <li><b-button @click="createEntrInfo" class="btn-primary">등록</b-button></li>
                                </ul>
                            </div>
                            <div class="card-body table-responsive">
                                <b-table
                                        selectable
                                        :select-mode="'single'"
                                        @row-selected="onRowSelected"
                                        :items="entrItems"
                                        :fields="entrFields"
                                        ref="entrTable"
                                >
                                    <template v-slot:action="{item}">
                                        <b-button
                                                variant="danger"
                                                @click="updateEntrInfo(item)"
                                        >수정</b-button>
                                    </template>
                                </b-table>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="card">
                            <div class="card-header pb-0">
                                <h3>등록된 부가서비스</h3>
                                <ul class="list-inline float-right m-0">
                                    <li><b-button @click.prevent="saveMenuMappingInfo" class="btn-primary">저장</b-button></li>
                                </ul>
                            </div>
                            <div class="card-body table-responsive">
                                <b-table
                                        :items="addInfoItems"
                                        :fields="addInfoFields"
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
                                </b-table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <modal-entr-info
                :selected-entr-info="selectedEntrInfo"
                :state="modalState"
        />
    </section>
</template>

<script>
    import ModalEntrInfo from '~/components/pages/entr/ModalEntrInfo.vue';
    export default {
        layout: 'default',
        name: "index",
        components:{ModalEntrInfo},

        data() {
            return {
                selectedEntrInfo: {},
                modalState: '',
                entrItems: [],
                entrFields: [
                    {key: 'entrItemNm', label: '가입가능요금제명'},
                    {key: 'socTypeCode', label: 'SOC타입코드',
                        formatter:(value)=> value === 'R' ? '부가서비스' : '요금제'},
                    {key: 'prodCd', label: '서비스코드',
                        formatter:(value)=> this.getProdCdName(value)},
                    {key: 'statusCd', label: '상태코드',
                        formatter:(value)=> value === '1' ? '사용' : '미사용'},
                    {key: 'action', label: '수정'},
                ],

                addInfoItems: [],
                addInfoFields: [
                    {key: 'addItemNm', label: '부가서비스명'},
                    {key: 'addItemCode', label: '부가서비스코드'},
                    {key: 'select', label: '사용여부'},
                ],
                searchUseYn: ''
            };
        },

        async beforeMount() {
            this.selectEntrItemList();
            this.getAddItemList();
        },

        async created() {
            this.$eventBus.$on('refreshEntrList', this.refreshList);
        },

        methods:{
            getProdCdName(prodCd) {
                switch (prodCd) {
                    case 'LZP0000001':
                        return '모바일';
                        break;
                    case 'LZP0000601':
                        return '인터넷';
                        break;
                    case 'LZP0000701':
                        return 'IPTV';
                        break;
                }
            },

            changeCheckbox() {
                this.$nextTick(_=>{
                    this.refreshList();
                })
            },

            refreshList() {
                this.getAddItemList();
                this.selectEntrItemList();
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

            async selectMappingList(entrItemCode) {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/entr/selectEntrMappingList', { entrItemCode });
                    return  data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async insertMapItemList(entrItemCode, addItemCodeList) {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/entr/insertMapItemList', { entrItemCode, addItemCodeList});
                    await this.$bvModal.msgBoxOk(data.message);

                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async updateEntrInfo(item) {
                this.modalState = "UPDATE";
                this.selectedEntrInfo = item;
                this.$nextTick(async () => {
                    this.$bvModal.show('modal_entrInfo');
                });
            },

            async createEntrInfo() {
                this.modalState = "CREATE";
                this.$nextTick(async () => {
                    this.$bvModal.show('modal_entrInfo');
                });

            },

            async saveMenuMappingInfo() {
                const addItemCodeList = this.addInfoItems.filter(item => item.checked === 'Y')
                                                 .map(item => item.addItemCode);

                const selectedEntrInfo = this.entrItems.find((entr,i)=>this.$refs.entrTable.isRowSelected(i));

                if(!selectedEntrInfo) {
                    await this.$bvModal.msgBoxOk('등록된 가입가능 요급제를 선택해주세요');
                    return;
                }

                if(!await this.$bvModal.msgBoxConfirm('운영 중인 시스템에 영향이 있습니다. 변경하시겠습니까?')) return;

                const message = await this.insertMapItemList(selectedEntrInfo.entrItemCode, addItemCodeList);
                await this.$bvModal.msgBoxOk(message);
            },

            async onRowSelected(items) {
                const selectedItem = items && items.length && items[0];
                if(!selectedItem.entrItemCode || selectedItem.statusCd !== '1') {
                    this.$refs.entrTable.clearSelected();
                    return;
                }

                const entrMappingList = await this.selectMappingList(selectedItem.entrItemCode);
                console.log('entrMappingList', entrMappingList);
                this.addInfoItems.forEach(item => this.$set(item, 'checked', 'N'));

                entrMappingList.forEach(mapping=>{
                    const item = this.addInfoItems.find(item => item.addItemCode === mapping.addItemCode);
                    if(!item) return;

                    this.$set(item, 'checked', 'Y');
                })
            }
        }
    }
</script>

<style scoped>

</style>