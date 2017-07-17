package org.toradocu.translator;

import org.toradocu.extractor.DocumentedExecutable;
import org.toradocu.extractor.ThrowsTag;
import org.toradocu.translator.spec.Specification;

public class ThrowsTranslator implements Translator<ThrowsTag> {

  @Override
  public Specification translate(ThrowsTag tag, DocumentedExecutable excMember) {
    return BasicTranslator.translate(tag, excMember);
  }
}
