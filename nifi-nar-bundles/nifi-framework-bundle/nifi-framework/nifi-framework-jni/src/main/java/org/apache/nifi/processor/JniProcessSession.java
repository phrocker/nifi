package org.apache.nifi.processor;

import org.apache.nifi.controller.queue.QueueSize;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.exception.FlowFileAccessException;
import org.apache.nifi.processor.io.InputStreamCallback;
import org.apache.nifi.processor.io.OutputStreamCallback;
import org.apache.nifi.processor.io.StreamCallback;
import org.apache.nifi.provenance.ProvenanceReporter;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class JniProcessSession implements ProcessSession {


    private long nativePtr;

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void rollback(boolean penalize) {

    }

    @Override
    public void migrate(ProcessSession newOwner, Collection<FlowFile> flowFiles) {

    }

    @Override
    public void adjustCounter(String name, long delta, boolean immediate) {

    }

    @Override
    public native FlowFile get();

    @Override
    public List<FlowFile> get(int maxResults) {
        return null;
    }

    @Override
    public List<FlowFile> get(FlowFileFilter filter) {
        return null;
    }

    @Override
    public QueueSize getQueueSize() {
        return null;
    }

    @Override
    public native FlowFile create();

    @Override
    public FlowFile create(FlowFile parent) {
        return null;
    }

    @Override
    public FlowFile create(Collection<FlowFile> parents) {
        return null;
    }

    @Override
    public FlowFile clone(FlowFile example) {
        return null;
    }

    @Override
    public FlowFile clone(FlowFile parent, long offset, long size) {
        return null;
    }

    @Override
    public FlowFile penalize(FlowFile flowFile) {
        return null;
    }

    @Override
    public native FlowFile putAttribute(FlowFile flowFile, String key, String value);

    @Override
    public FlowFile putAllAttributes(FlowFile flowFile, Map<String, String> attributes) {
        for(Map.Entry<String,String> entry : attributes.entrySet()){
            putAttribute(flowFile,entry.getKey(),entry.getValue());
        }
        return flowFile;
    }

    @Override
    public FlowFile removeAttribute(FlowFile flowFile, String key) {
        return null;
    }

    @Override
    public FlowFile removeAllAttributes(FlowFile flowFile, Set<String> keys) {
        return null;
    }

    @Override
    public FlowFile removeAllAttributes(FlowFile flowFile, Pattern keyPattern) {
        return null;
    }

    @Override
    public native void transfer(FlowFile flowFile, Relationship relationship);

    @Override
    public void transfer(FlowFile flowFile) {

    }

    @Override
    public void transfer(Collection<FlowFile> flowFiles) {

    }

    @Override
    public void transfer(Collection<FlowFile> flowFiles, Relationship relationship) {

    }

    @Override
    public void remove(FlowFile flowFile) {

    }

    @Override
    public void remove(Collection<FlowFile> flowFiles) {

    }

    @Override
    public void read(FlowFile source, InputStreamCallback reader) throws FlowFileAccessException {

    }

    @Override
    public InputStream read(FlowFile flowFile) {
        return null;
    }

    @Override
    public void read(FlowFile source, boolean allowSessionStreamManagement, InputStreamCallback reader) throws FlowFileAccessException {

    }

    @Override
    public FlowFile merge(Collection<FlowFile> sources, FlowFile destination) {
        return null;
    }

    @Override
    public FlowFile merge(Collection<FlowFile> sources, FlowFile destination, byte[] header, byte[] footer, byte[] demarcator) {
        return null;
    }

    @Override
    public FlowFile write(FlowFile source, OutputStreamCallback writer) throws FlowFileAccessException {
        return null;
    }

    @Override
    public OutputStream write(FlowFile source) {
        return null;
    }

    @Override
    public FlowFile write(FlowFile source, StreamCallback writer) throws FlowFileAccessException {
        return null;
    }

    @Override
    public FlowFile append(FlowFile source, OutputStreamCallback writer) throws FlowFileAccessException {
        return null;
    }

    @Override
    public FlowFile importFrom(Path source, boolean keepSourceFile, FlowFile destination) {
        return null;
    }

    @Override
    public FlowFile importFrom(InputStream source, FlowFile destination) {
        return null;
    }

    @Override
    public void exportTo(FlowFile flowFile, Path destination, boolean append) {

    }

    @Override
    public void exportTo(FlowFile flowFile, OutputStream destination) {

    }

    @Override
    public ProvenanceReporter getProvenanceReporter() {
        return new JniProvenanceReporter();
    }
}
