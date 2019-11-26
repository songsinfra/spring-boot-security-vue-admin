<template>
    <b-modal
            id="modal_update_up_menu"
            centered
            @ok.prevent="submit"
            ref="modal"
            @show="showModal"
            :title="title">
        <b-form>
            <b-form-group
                    id="upMenuName-group-1"
                    label="상위메뉴명:"
                    label-for="upMenuName-1"
            >
                <b-form-input
                        id="upMenuName-1"
                        v-model="upMenu.upMenuName"
                        v-validate="'required'"
                        data-vv-name="상위메뉴명"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('상위메뉴명')">
                    {{errors.first('상위메뉴명')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    id="useYn-group-1"
                    label="사용여부:"
                    label-for="useYn-1"
            >
                <b-form-select
                        id="useYn-1"
                        :required="true"
                        v-model="upMenu.useYn"
                        :options="useYnOption"
                        v-validate="'required'"
                        data-vv-name="사용여부"
                ></b-form-select>
                <b-form-invalid-feedback :state="!errors.has('사용여부')">
                    {{errors.first('사용여부')}}
                </b-form-invalid-feedback>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        props: ['selectedMenu', 'state'],
        name: "modalUpMenu",
        data() {
            return {
                upMenu: {},
                title : ''
            };
        },
        computed: {
            useYnOption() {
                return this.$store.state.menu.useYnList;
            }
        },

        methods:{
            async submit() {
                if(!await this.$validator.validate()) {
                    console.dir(this.$validator);
                    return;
                }
                await this.ok();
            },

            async ok() {
                try {
                    const url = this.$props.state === 'CREATE' ? "/menu/setUpMenuAdd" : "/menu/setMenuUpdate";
                    const response = await this.$axios.$post( process.env.contextPath + url, {
                        menuId: this.upMenu.menuId,
                        menuName: this.upMenu.upMenuName,
                        menuURL: this.upMenu.menuURL,
                        ordNo: this.upMenu.ordNo,
                        useYn: this.upMenu.useYn
                    });

                    await this.$bvModal.msgBoxOk("저장이 완료 되었습니다.");
                    await this.$store.dispatch('menu/addUpMenuList');

                    this.$eventBus.$emit('refreshGrid');
                    this.$refs.modal.hide();
                } catch (e) {
                    console.dir(e);
                    e = (e.response && e.response.data) || e;

                    await this.$bvModal.msgBoxOk(e.message);
                }
            },
            async showModal() {
                try {
                    this.errors.clear();
                    this.$nextTick(async () => {
                        if (this.$props.state === 'CREATE') {
                            this.upMenu = {useYn:'Y'};
                            this.title = '상위메뉴 등록';
                            return;
                        }

                        this.title = "상위메뉴 수정";
                        this.upMenu = {...this.$props.selectedMenu};
                    });
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            }
        }
    }
</script>

<style scoped>

</style>