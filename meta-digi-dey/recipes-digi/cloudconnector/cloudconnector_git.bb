# Copyright (C) 2017-2022, Digi International Inc.

SUMMARY = "Digi's device cloud connector"
SECTION = "libs"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MPL-2.0;md5=815ca599c9df247a0c7f619bab123dad"

DEPENDS = "confuse libdigiapix openssl recovery-utils swupdate zlib json-c"

SRCBRANCH = "master"
SRCREV = "${AUTOREV}"

CC_STASH = "gitsm://git@stash.digi.com/cc/cc_dey.git;protocol=ssh"
CC_GITHUB = "gitsm://github.com/digi-embedded/cc_dey.git;protocol=https"

CC_GIT_URI ?= "${@oe.utils.conditional('DIGI_INTERNAL_GIT', '1' , '${CC_STASH}', '${CC_GITHUB}', d)}"

SRC_URI = " \
    ${CC_GIT_URI};branch=${SRCBRANCH} \
    file://cloud-connector-init \
    file://cloud-connector.service \
"

S = "${WORKDIR}/git"

inherit pkgconfig systemd update-rc.d

do_install() {
	oe_runmake DESTDIR=${D} install

	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		# Install systemd unit files
		install -d ${D}${systemd_unitdir}/system
		install -m 0644 ${WORKDIR}/cloud-connector.service ${D}${systemd_unitdir}/system/
	fi

	if ${@oe.utils.vartrue('DUALBOOT_ENABLED', 'true', 'false', d)}; then
		sed -i "/firmware_download_path = \/mnt\/update/c\firmware_download_path = \/home\/root" ${D}${sysconfdir}/cc.conf
	fi

	install -d ${D}${sysconfdir}/init.d/
	install -m 755 ${WORKDIR}/cloud-connector-init ${D}${sysconfdir}/cloud-connector
	ln -sf /etc/cloud-connector ${D}${sysconfdir}/init.d/cloud-connector
}

INITSCRIPT_NAME = "cloud-connector"
SYSTEMD_SERVICE:${PN} = "cloud-connector.service"

PACKAGES =+ "${PN}-cert"

FILES:${PN} += " \
    ${systemd_unitdir}/system/cloud-connector.service \
    ${sysconfdir}/cloud-connector \
    ${sysconfdir}/init.d/cloud-connector \
"

FILES:${PN}-cert = "${sysconfdir}/ssl/certs/Digi_Int-ca-cert-public.crt"

CONFFILES:${PN} += "${sysconfdir}/cc.conf"

RDEPENDS:${PN} = "${PN}-cert"

# Disable extra compilation checks from SECURITY_CFLAGS to avoid build errors
lcl_maybe_fortify:pn-cloudconnector = ""
