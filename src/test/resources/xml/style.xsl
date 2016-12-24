<?xml version="1.0" encoding="UTF-8"?>
<!--This style sheet alphabetizes the items in a style resource according to attribute-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output encoding="UTF-8" method="xml" omit-xml-declaration="no" indent="yes"/>

    <xsl:template match="/">
        <xsl:text>&#10;</xsl:text>
        <xsl:copy-of select="/comment()"/>
        <xsl:text>&#10;</xsl:text>
        <resources>
            <!--<xsl:copy-of select="resources/comment()"/>-->
            <xsl:apply-templates select="resources/style"/>
        </resources>
    </xsl:template>

    <xsl:template match="/resources/style">
        <xsl:text>&#10;&#9;</xsl:text>
        <xsl:copy-of select="/resources/comment()"/>
        <xsl:text>&#10;&#9;</xsl:text>
        <style name="{@name}">
            <xsl:for-each select="/resources/style/item">
                <xsl:sort select="@name"/>
                <xsl:text>&#10;&#9;&#9;</xsl:text>
                <item name="{@name}"><xsl:copy-of select="text()"/></item>
            </xsl:for-each>
        </style>
    </xsl:template>

</xsl:stylesheet>