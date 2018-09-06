package org.superbiz.moviefun.store;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.superbiz.moviefun.Blob;

import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {

    private AmazonS3 s3Client;

    private String bucket;

    public S3Store(AmazonS3 s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.bucket = photoStorageBucket;

        System.out.println("********** BUCKET NAME:: " + bucket);
        if(s3Client.doesBucketExist(bucket)) {
            System.out.println("Bucket name is not available."
                    + " Try again with a different Bucket name.");

        } else {
            System.out.println("Bucket  available");
            s3Client.createBucket(bucket);
        }

    }

    @Override
    public void put(Blob blob) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        s3Client.putObject(bucket, blob.name, blob.inputStream, objectMetadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {

        S3Object s3Object = s3Client.getObject(bucket, name);

        if(s3Object != null) {
            Blob blob = new Blob(name, s3Object.getObjectContent(), s3Object.getObjectMetadata().getContentType());
            return Optional.of(blob);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        s3Client.deleteBucket(bucket);
    }


}
