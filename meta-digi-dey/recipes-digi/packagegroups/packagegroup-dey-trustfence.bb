# Copyright (C) 2016 Digi International.

SUMMARY = "DEY trustfence packagegroup"

inherit packagegroup

RDEPENDS_${PN} = "\
	${@base_conditional('TRUSTFENCE_CONSOLE_DISABLE', '1', 'auto-serial-console', '', d)} \
"
do_package[vardeps] += "TRUSTFENCE_CONSOLE_DISABLE"
