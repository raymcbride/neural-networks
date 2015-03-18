<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
 <html>
 <title>neural-networks</title>
  <body>
  <h2 align="center">Financial Times Stock Exchange 100 Index</h2>
  <p align="center">
  <table border="1" cellpadding="5">
      <tr>
        <th>Date</th>
        <th>Index Value</th>
      </tr>
      <xsl:for-each select="project/ftse">
      <tr>
        <td><xsl:value-of select="date" /></td>
        <td><xsl:value-of select="indexValue" /></td>
      </tr>
      </xsl:for-each>
    </table>
  </p>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>