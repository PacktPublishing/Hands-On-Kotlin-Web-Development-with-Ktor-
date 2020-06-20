package com.example

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.content.TextContent
import io.ktor.features.ContentConverter
import io.ktor.features.suitableCharset
import io.ktor.http.ContentType
import io.ktor.http.withCharset
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.contentCharset
import io.ktor.util.pipeline.PipelineContext
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.jvm.javaio.toInputStream

class XmlConverter : ContentConverter{
    override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
        val request = context.subject
        val channel = request.value as? ByteChannel ?: return null
        val reader = channel.toInputStream().reader(context.call.request.contentCharset() ?: Charsets.UTF_8)
        val type = request.type
        val xmlMapper = XmlMapper()
        val xml = reader
        val result: Any? = xmlMapper.readValue(xml, type.javaObjectType)
        return result
    }

    override suspend fun convertForSend(
        context: PipelineContext<Any, ApplicationCall>,
        contentType: ContentType,
        value: Any
    ): Any? {
        val xmlMapper = XmlMapper()
        val xml = xmlMapper.writeValueAsString(value)
        return TextContent(xml, contentType.withCharset(context.call.suitableCharset()))
    }
}