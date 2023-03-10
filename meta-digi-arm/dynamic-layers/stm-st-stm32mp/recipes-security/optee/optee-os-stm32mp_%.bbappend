#
# Copyright (C) 2022 Digi International Inc.
#

# Select internal or Github OPTEE repo
OPTEE_URI_STASH = "${DIGI_MTK_GIT}/emp/optee_os.git;protocol=ssh"
OPTEE_URI_GITHUB = "${DIGI_GITHUB_GIT}/optee_os.git;protocol=https"
OPTEE_GIT_URI ?= "${@oe.utils.conditional('DIGI_INTERNAL_GIT', '1' , '${OPTEE_URI_STASH}', '${OPTEE_URI_GITHUB}', d)}"

SRCBRANCH = "3.16.0/stm/maint"
SRCREV = "61357ddb2b3d2464ec0fedaa9aa6ec038e56cc2a"

SRC_URI = " \
    ${OPTEE_GIT_URI};nobranch=1;name=os \
    file://fonts.tar.gz;subdir=git;name=fonts \
"

# Enable OTP write support
EXTRA_OEMAKE += "CFG_STM32_BSEC_WRITE=y"
